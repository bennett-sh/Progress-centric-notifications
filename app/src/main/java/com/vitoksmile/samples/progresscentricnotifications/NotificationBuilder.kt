package com.vitoksmile.samples.progresscentricnotifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.core.content.getSystemService

interface NotificationBuilder {

    fun show(orderStatus: OrderStatus)
}

class NotificationBuilderImpl(
    private val context: Context,
) : NotificationBuilder {

    override fun show(orderStatus: OrderStatus) = show { builder ->
        // Two segments: cooking and delivering
        // We don't want to show first "fast" statuses (placed and accepted)
        val cookingSegment = Notification.ProgressStyle.Segment(100)
            .setColor(context.getColor(R.color.order_status_cooking))
        val deliveringSegment = Notification.ProgressStyle.Segment(100)
            .setColor(context.getColor(R.color.order_status_delivering))
        val progressSegments = listOf(
            cookingSegment,
            deliveringSegment,
        )

        val progress = (when (orderStatus) {
            is OrderStatus.Placed -> 0
            is OrderStatus.Accepted -> 0
            is OrderStatus.Cooking -> (orderStatus.progress * 100).toInt()
            is OrderStatus.Cooked -> cookingSegment.length
            is OrderStatus.Delivering -> cookingSegment.length + (orderStatus.progress * 100).toInt()
            is OrderStatus.Delivered -> cookingSegment.length + deliveringSegment.length
        }).coerceIn(minimumValue = 0, maximumValue = progressSegments.sumOf { it.length })

        val style = Notification.ProgressStyle()
            // Indicates whether the segments and points will be styled differently based
            // on whether they are behind or ahead of the current progress.
            .setStyledByProgress(false)
            // Specifies the progress (in the same units as Segment.getLength())
            // of the tracker along the length of the bar.
            .setProgress(progress)
            // Icon that can appear as an overlay on the bar at the point of current progress.
            //.setProgressTrackerIcon(Icon.createWithResource(context, icon))
            .setProgressSegments(progressSegments)
            .setProgressPoints(
                // Set point between cooked and delivering statuses
                listOf(
                    Notification.ProgressStyle.Point(cookingSegment.length)
                        .setColor(context.getColor(R.color.order_status_cooked))
                )
            )

        // Change icon based on the status
        when (orderStatus) {
            is OrderStatus.Placed -> {
                builder.setSmallIcon(R.drawable.ic_notification_placed)
            }

            is OrderStatus.Accepted -> {
                builder.setSmallIcon(R.drawable.ic_notification_accepted)
            }

            is OrderStatus.Cooking -> {
                builder.setSmallIcon(R.drawable.ic_notification_cooking)
                style.setProgressTrackerIcon(R.drawable.ic_status_cooking)
                builder.style = style
            }

            is OrderStatus.Cooked -> {
                builder.setSmallIcon(R.drawable.ic_notification_packaging)
                style.setProgressTrackerIcon(R.drawable.ic_status_packaging)
                builder.style = style
            }

            is OrderStatus.Delivering -> {
                builder.setSmallIcon(R.drawable.ic_notification_delivering)
                style.setProgressTrackerIcon(R.drawable.ic_status_delivering)
                builder.style = style
            }

            is OrderStatus.Delivered -> {
                builder.setSmallIcon(R.drawable.ic_notification_delivered)
            }
        }

        val title = when (orderStatus) {
            is OrderStatus.Placed -> {
                "Order is placed"
            }

            is OrderStatus.Accepted -> {
                "Order is accepted"
            }

            is OrderStatus.Cooking -> {
                "Order is being cooked"
            }

            is OrderStatus.Cooked -> {
                "Order is cooked"
            }

            is OrderStatus.Delivering -> {
                builder.setSubText(orderStatus.distance)
                "Order arriving in ${orderStatus.duration}"
            }

            is OrderStatus.Delivered -> {
                "Delivered"
            }
        }
        val text = when (orderStatus) {
            is OrderStatus.Placed -> {
                "We will start the cooking soon"
            }

            is OrderStatus.Accepted -> {
                "We will start the cooking soon"
            }

            is OrderStatus.Cooking -> {
                "Will be cooked soon"
            }

            is OrderStatus.Cooked -> {
                "Will be delivered soon"
            }

            is OrderStatus.Delivering -> {
                "${orderStatus.courierName} is delivering your order"
            }

            is OrderStatus.Delivered -> {
                "Bon appetit"
            }
        }

        builder
            .setContentTitle(title)
            .setContentText(text)
            .build()
    }

    private inline fun show(block: (Notification.Builder) -> Unit) {
        val notificationManager = context.getSystemService<NotificationManager>()
            ?: error("Can't find NotificationManager")

        val channelId = "progress.centric"
        val channel = NotificationChannel(
            channelId,
            context.getString(R.string.notification_channel_title),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        notificationManager.createNotificationChannel(channel)

        val notification = Notification.Builder(context, channelId)
            .apply(block)
            .build()
        notificationManager.notify(1, notification)
    }

    private fun Notification.ProgressStyle.setProgressTrackerIcon(
        @DrawableRes resId: Int,
    ): Notification.ProgressStyle {
        return setProgressTrackerIcon(Icon.createWithResource(context, resId))
    }
}
