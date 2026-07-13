package com.example.shelfy.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.shelfy.data.preferences.NotificationKeys
import com.example.shelfy.data.preferences.settingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prefs = context.settingsDataStore.data.first()
                val enabled = prefs[NotificationKeys.NOTIFICATIONS_ENABLED] ?: true
                if (enabled) {
                    val hour = prefs[NotificationKeys.ALERT_HOUR] ?: 12
                    val minute = prefs[NotificationKeys.ALERT_MINUTE] ?: 0
                    NotificationScheduler.schedule(context, hour, minute)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
