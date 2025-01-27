package com.vitoksmile.samples.progresscentricnotifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppContent() {
    val permissionState = rememberPermissionState()

    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            OrderStatusContent()
        }

        is PermissionStatus.Denied -> {
            PermissionDeniedView(
                onRequestPermissionClick = permissionState::launchPermissionRequest,
            )
        }
    }
}

@Composable
private fun PermissionDeniedView(
    onRequestPermissionClick: () -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text("Notification permission is required")
            OutlinedButton(
                onClick = onRequestPermissionClick,
            ) {
                Text("Request permission")
            }
        }
    }
}
