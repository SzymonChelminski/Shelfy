package com.example.shelfy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shelfy.data.local.ShelfyDatabase
import com.example.shelfy.notifications.NotificationHelper
import java.util.Calendar
import java.util.TimeZone

suspend fun checkExpiringProducts(context: Context) {
    val dao = ShelfyDatabase.getInstance(context).scannedProductDao()

    // DatePicker stores dates as UTC midnight, so compare against UTC day boundaries
    val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val oneDayMillis = 24 * 60 * 60 * 1000L
    val startOfTomorrow = utcCal.timeInMillis + oneDayMillis

    val products = dao.getProductsExpiringBetween(
        from = startOfTomorrow,
        to   = startOfTomorrow + oneDayMillis
    )

    if (products.isNotEmpty()) {
        NotificationHelper.showExpiryNotification(context, products)
    }
}

class ExpiryCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            checkExpiringProducts(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
