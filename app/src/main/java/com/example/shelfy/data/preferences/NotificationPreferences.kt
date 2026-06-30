package com.example.shelfy.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "notification_settings")

object NotificationKeys {
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val ALERT_HOUR = intPreferencesKey("alert_hour")
    val ALERT_MINUTE = intPreferencesKey("alert_minute")
    val ADVANCE_DAYS = intPreferencesKey("advance_warning_days")
}

object HabitKeys {
    val CONSUMED_COUNT = intPreferencesKey("consumed_count")
    val THROWN_COUNT = intPreferencesKey("thrown_count")
}

