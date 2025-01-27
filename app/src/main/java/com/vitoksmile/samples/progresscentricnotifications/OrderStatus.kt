package com.vitoksmile.samples.progresscentricnotifications

import androidx.annotation.FloatRange
import kotlin.time.Duration

sealed interface OrderStatus {

    data object Placed : OrderStatus

    data object Accepted : OrderStatus

    data class Cooking(
        @get:FloatRange(from = 0.0, to = 1.0)
        val progress: Float,
    ) : OrderStatus

    data object Cooked : OrderStatus

    data class Delivering(
        @get:FloatRange(from = 0.0, to = 1.0)
        val progress: Float,
        val duration: Duration,
        val distance: String,
        val courierName: String,
    ) : OrderStatus

    data object Delivered : OrderStatus
}
