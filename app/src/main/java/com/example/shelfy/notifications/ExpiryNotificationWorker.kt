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
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shelfy.R
import com.example.shelfy.data.local.ShelfyDatabase
import com.example.shelfy.data.preferences.NotificationKeys
import com.example.shelfy.data.preferences.settingsDataStore
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.TimeZone

class ExpiryNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val prefs = applicationContext.settingsDataStore.data.first()
        val enabled = prefs[NotificationKeys.NOTIFICATIONS_ENABLED] ?: true
        if (!enabled) return Result.success()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val advanceDays = prefs[NotificationKeys.ADVANCE_DAYS] ?: 1

        // Dates stored as UTC midnight — find products whose expiry date is exactly (today + advanceDays)
        val dayMs = 24 * 60 * 60 * 1000L
        val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetStart = utcCal.timeInMillis + advanceDays * dayMs
        val targetEnd = targetStart + dayMs

        val dao = ShelfyDatabase.getInstance(applicationContext).scannedProductDao()
        val expiring = dao.getProductsExpiringBefore(targetEnd)
            .filter { it.expiryDateMillis >= targetStart }

        if (expiring.isEmpty()) return Result.success()

        ensureChannel()

        val dayLabel = if (advanceDays == 1) "tomorrow" else "in $advanceDays days"
        val title = if (expiring.size == 1)
            "${expiring[0].name} expires $dayLabel"
        else
            "${expiring.size} products expire $dayLabel"
        val body = expiring.joinToString("\n") { "• ${it.name}" }

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(0xFF22C55E.toInt())
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (expiring.size > 1) {
            builder.setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())
        return Result.success()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Expiry Alerts", NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Alerts when products are about to expire" }
            applicationContext.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "shelfy_expiry"
        private const val NOTIFICATION_ID = 1001
    }
}
