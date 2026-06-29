package com.example.shelfy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shelfy.di.DatabaseModule
import com.example.shelfy.notifications.NotificationHelper
import com.example.shelfy.ui.AppNavigation
import com.example.shelfy.ui.theme.ShelfyTheme
import com.example.shelfy.workers.ExpiryCheckWorker
import com.example.shelfy.workers.checkExpiringProducts
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseModule.init(this)
        NotificationHelper.createChannel(this)
        scheduleExpiryCheck()
        lifecycleScope.launch { checkExpiringProducts(applicationContext) }
        setContent {
            ShelfyTheme {
                AppNavigation()
            }
        }
    }

    private fun scheduleExpiryCheck() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "expiry_check",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<ExpiryCheckWorker>(24, TimeUnit.HOURS).build()
        )
    }
}
