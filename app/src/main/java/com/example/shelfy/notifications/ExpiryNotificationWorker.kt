package com.example.shelfy.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shelfy.R
import com.example.shelfy.data.local.ShelfyDatabase
import com.example.shelfy.data.preferences.settingsDataStore
import kotlinx.coroutines.flow.first

class ExpiryNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val prefs = applicationContext.settingsDataStore.data.first()
        val enabled = prefs[booleanPreferencesKey("notifications_enabled")] ?: true
        if (!enabled) return Result.success()

        val advanceDays = prefs[intPreferencesKey("advance_warning_days")] ?: 2
        val threshold = System.currentTimeMillis() + advanceDays * 24L * 60 * 60 * 1000

        val dao = ShelfyDatabase.getInstance(applicationContext).scannedProductDao()
        val expiring = dao.getProductsExpiringBefore(threshold)

        if (expiring.isNotEmpty()) {
            ensureChannel()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) return Result.success()

            val firstName = expiring[0].name
            val text: String = if (expiring.size == 1)
                "$firstName expires within $advanceDays day(s)!"
            else
                "${expiring.size} products are expiring within $advanceDays day(s)!"

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Shelfy – Expiry Alert")
                .setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        }

        return Result.success()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Expiry Alerts", NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Alerts when products are about to expire" }
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "shelfy_expiry"
        private const val NOTIFICATION_ID = 1001
    }
}
