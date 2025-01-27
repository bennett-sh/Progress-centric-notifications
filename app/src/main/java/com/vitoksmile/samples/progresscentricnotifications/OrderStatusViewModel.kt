package com.vitoksmile.samples.progresscentricnotifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class OrderStatusViewModel(
    private val notificationBuilder: NotificationBuilder,
) : ViewModel() {

    private val _orderStatus = MutableStateFlow<OrderStatus?>(null)
    val orderStatus: StateFlow<OrderStatus?> = _orderStatus.asStateFlow()

    private var animateJob: Job? = null

    fun animate() {
        animateJob?.cancel()
        animateJob = viewModelScope.launch {
            delay(1.seconds)
            update(OrderStatus.Placed)
            delay(1.seconds)
            update(OrderStatus.Accepted)
            delay(1.seconds)
            repeat(100) { index ->
                update(OrderStatus.Cooking(progress = (index + 1) / 100f))
                delay(20.milliseconds)
            }
            update(OrderStatus.Cooked)
            delay(1.seconds)
            repeat(100) { index ->
                update(
                    OrderStatus.Delivering(
                        progress = (index + 1) / 100f,
                        duration = (100 - index).minutes,
                        distance = "${100 - index}0 m",
                        courierName = "Viktor",
                    )
                )
                delay(20.milliseconds)
            }
            delay(1.seconds)
            update(OrderStatus.Delivered)
        }
    }

    fun set(orderStatus: OrderStatus) {
        animateJob?.cancel()
        update(orderStatus)
    }

    private fun update(orderStatus: OrderStatus) {
        _orderStatus.update { orderStatus }
        notificationBuilder.show(orderStatus)
    }

    // Define ViewModel factory in a companion object
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                return OrderStatusViewModel(
                    notificationBuilder = NotificationBuilderImpl(context = application)
                ) as T
            }
        }
    }
}
