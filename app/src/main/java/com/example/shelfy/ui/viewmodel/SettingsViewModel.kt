package com.example.shelfy.ui.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shelfy.data.preferences.HabitKeys
import com.example.shelfy.data.preferences.NotificationKeys
import com.example.shelfy.data.preferences.settingsDataStore
import com.example.shelfy.di.DatabaseModule
import com.example.shelfy.notifications.NotificationScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.settingsDataStore

    val notificationsEnabled = dataStore.data
        .map { it[NotificationKeys.NOTIFICATIONS_ENABLED] ?: true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val alertHour = dataStore.data
        .map { it[NotificationKeys.ALERT_HOUR] ?: 12 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 12)

    val alertMinute = dataStore.data
        .map { it[NotificationKeys.ALERT_MINUTE] ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val advanceWarningDays = dataStore.data
        .map { it[NotificationKeys.ADVANCE_DAYS] ?: 1 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 1)

    val consumedCount = dataStore.data
        .map { it[HabitKeys.CONSUMED_COUNT] ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val thrownCount = dataStore.data
        .map { it[HabitKeys.THROWN_COUNT] ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { it[NotificationKeys.NOTIFICATIONS_ENABLED] = enabled }
            val ctx = getApplication<Application>().applicationContext
            if (enabled) {
                NotificationScheduler.schedule(ctx, alertHour.value, alertMinute.value)
            } else {
                NotificationScheduler.cancel(ctx)
            }
        }
    }

    fun setAlertTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            dataStore.edit {
                it[NotificationKeys.ALERT_HOUR] = hour
                it[NotificationKeys.ALERT_MINUTE] = minute
            }
            if (notificationsEnabled.value) {
                val ctx = getApplication<Application>().applicationContext
                NotificationScheduler.schedule(ctx, hour, minute)
            }
        }
    }

    fun setAdvanceWarningDays(days: Int) {
        viewModelScope.launch {
            dataStore.edit { it[NotificationKeys.ADVANCE_DAYS] = days }
        }
    }

    fun recordConsumed() {
        viewModelScope.launch {
            dataStore.edit { it[HabitKeys.CONSUMED_COUNT] = (it[HabitKeys.CONSUMED_COUNT] ?: 0) + 1 }
        }
    }

    fun recordThrown() {
        viewModelScope.launch {
            dataStore.edit { it[HabitKeys.THROWN_COUNT] = (it[HabitKeys.THROWN_COUNT] ?: 0) + 1 }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            DatabaseModule.repository.deleteAll()
            dataStore.edit {
                it[HabitKeys.CONSUMED_COUNT] = 0
                it[HabitKeys.THROWN_COUNT] = 0
            }
        }
    }

    suspend fun exportCsvFile(): File {
        val products = DatabaseModule.repository.getAllProducts().first()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val sb = StringBuilder("Name,Brand,Category,Quantity,Expiry Date\n")
        products.forEach { p ->
            sb.append("\"${p.name}\",\"${p.brand}\",\"${p.category}\",\"${p.quantity}\",\"${dateFormat.format(Date(p.expiryDateMillis))}\"\n")
        }
        val dir = File(getApplication<Application>().cacheDir, "export").also { it.mkdirs() }
        val file = File(dir, "shelfy_export.csv")
        file.writeText(sb.toString())
        return file
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    SettingsViewModel(application) as T
            }
    }
}
