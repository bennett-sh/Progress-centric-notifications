package com.vitoksmile.samples.progresscentricnotifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.time.Duration.Companion.minutes

@Composable
fun OrderStatusContent() {
    val viewModel: OrderStatusViewModel = viewModel(factory = OrderStatusViewModel.Factory)
    val orderStatus by viewModel.orderStatus.collectAsStateWithLifecycle()

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text("Order status: $orderStatus", textAlign = TextAlign.Center)

            OutlinedButton(
                onClick = {
                    viewModel.animate()
                }
            ) {
                Text("Animate")
            }

            Button(
                onClick = {
                    viewModel.set(OrderStatus.Placed)
                }
            ) {
                Text("Placed")
            }

            Button(
                onClick = {
                    viewModel.set(OrderStatus.Accepted)
                }
            ) {
                Text("Accepted")
            }

            Button(
                onClick = {
                    viewModel.set(OrderStatus.Cooking(progress = 0.33f))
                }
            ) {
                Text("Cooking")
            }

            Button(
                onClick = {
                    viewModel.set(OrderStatus.Cooked)
                }
            ) {
                Text("Cooked")
            }

            Button(
                onClick = {
                    viewModel.set(
                        OrderStatus.Delivering(
                            progress = 0.33f,
                            duration = 3.minutes,
                            distance = "750 m",
                            courierName = "Viktor",
                        )
                    )
                }
            ) {
                Text("Delivering")
            }

            Button(
                onClick = {
                    viewModel.set(OrderStatus.Delivered)
                }
            ) {
                Text("Delivered")
            }
        }
    }
}
