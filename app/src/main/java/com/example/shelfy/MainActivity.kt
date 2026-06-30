package com.example.shelfy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import com.example.shelfy.data.preferences.NotificationKeys
import com.example.shelfy.data.preferences.settingsDataStore
import com.example.shelfy.di.DatabaseModule
import com.example.shelfy.notifications.NotificationScheduler
import com.example.shelfy.ui.AppNavigation
import com.example.shelfy.ui.theme.ShelfyTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseModule.init(this)
        // Cancel legacy worker from the old notification system
        WorkManager.getInstance(this).cancelUniqueWork("expiry_check")
        autoScheduleNotifications()
        setContent {
            ShelfyTheme {
                AppNavigation()
            }
        }
    }

    private fun autoScheduleNotifications() {
        lifecycleScope.launch {
            val prefs = settingsDataStore.data.first()
            val enabled = prefs[NotificationKeys.NOTIFICATIONS_ENABLED] ?: true
            if (!enabled) return@launch
            val hour = prefs[NotificationKeys.ALERT_HOUR] ?: 12
            val minute = prefs[NotificationKeys.ALERT_MINUTE] ?: 0
            // KEEP policy: won't reset the schedule if already running
            NotificationScheduler.scheduleIfNotRunning(applicationContext, hour, minute)
        }
    }
}
