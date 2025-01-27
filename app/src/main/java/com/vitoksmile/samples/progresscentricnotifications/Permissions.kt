package com.vitoksmile.samples.progresscentricnotifications

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberPermissionState(
    permission: String = android.Manifest.permission.POST_NOTIFICATIONS,
): PermissionState {
    val permissionStatus = remember(permission) {
        mutableStateOf<PermissionStatus>(PermissionStatus.Denied)
    }

    fun onGranted(isGranted: Boolean) {
        permissionStatus.value =
            if (isGranted) PermissionStatus.Granted
            else PermissionStatus.Denied
    }

    val context = LocalContext.current
    LaunchedEffect(permission) {
        onGranted(
            isGranted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED,
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission(), ::onGranted)

    return remember {
        object : PermissionState {

            override val status: PermissionStatus
                get() = permissionStatus.value

            override fun launchPermissionRequest() {
                permissionLauncher.launch(permission)
            }
        }
    }
}

@Stable
interface PermissionState {

    val status: PermissionStatus

    fun launchPermissionRequest()
}

@Immutable
sealed interface PermissionStatus {

    data object Granted : PermissionStatus

    data object Denied : PermissionStatus
}
