package com.vitoksmile.samples.progresscentricnotifications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vitoksmile.samples.progresscentricnotifications.ui.theme.ProgressCentricNotificationsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgressCentricNotificationsTheme {
                AppContent()
            }
        }
    }
}
