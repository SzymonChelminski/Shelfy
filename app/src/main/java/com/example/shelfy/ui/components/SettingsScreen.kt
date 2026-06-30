package com.example.shelfy.ui.components

import android.content.Intent
import android.Manifest
import android.app.Application
import android.os.Build
import androidx.core.content.FileProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.example.shelfy.ui.theme.Error
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.viewmodel.SettingsViewModel
import com.example.shelfy.ui.theme.Text as ThemeText
import kotlinx.coroutines.launch

private val SettingsBackground = Brush.verticalGradient(
    colors = listOf(Color(0xFFE3F3E7), Color(0xFFF6FBF7))
)

private val DividerColor = Color(0xFFEFEFEF)

private val WARNING_DAY_OPTIONS = listOf(1, 2, 3, 5, 7)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(application))

    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val alertHour by viewModel.alertHour.collectAsStateWithLifecycle()
    val alertMinute by viewModel.alertMinute.collectAsStateWithLifecycle()
    val advanceDays by viewModel.advanceWarningDays.collectAsStateWithLifecycle()
    val consumedCount by viewModel.consumedCount.collectAsStateWithLifecycle()
    val thrownCount by viewModel.thrownCount.collectAsStateWithLifecycle()
    val totalCount = consumedCount + thrownCount
    val scope = rememberCoroutineScope()

    var showTimePicker by remember { mutableStateOf(false) }
    var showDaysPicker by remember { mutableStateOf(false) }
    var showClearConfirm by remember { mutableStateOf(false) }

    val notifPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { granted ->
                if (granted) viewModel.setNotificationsEnabled(true)
            }
        )
    } else null

    // Time picker dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = alertHour,
            initialMinute = alertMinute,
            is24Hour = true
        )
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Daily alert time",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ThemeText
                    )
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.setAlertTime(timePickerState.hour, timePickerState.minute)
                                showTimePicker = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) { Text("Save", color = Color.White) }
                    }
                }
            }
        }
    }

    // Clear all data confirmation dialog
    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear all data?", fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    "This will permanently delete all products from your inventory. This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ThemeText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllData()
                        showClearConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Error)
                ) { Text("Delete all", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text("Cancel") }
            }
        )
    }

    // Advance warning days dialog
    if (showDaysPicker) {
        var selectedDays by remember { mutableStateOf(advanceDays) }
        AlertDialog(
            onDismissRequest = { showDaysPicker = false },
            title = { Text("Advance warning", fontWeight = FontWeight.SemiBold) },
            text = {
                Column {
                    WARNING_DAY_OPTIONS.forEach { days ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = selectedDays == days,
                                onClick = { selectedDays = days },
                                colors = RadioButtonDefaults.colors(selectedColor = Primary)
                            )
                            Text(
                                text = if (days == 1) "1 day before" else "$days days before",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ThemeText
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setAdvanceWarningDays(selectedDays)
                        showDaysPicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) { Text("Save", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDaysPicker = false }) { Text("Cancel") }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SettingsBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        BackButton(onClick = onBack)

        SettingsSection(title = "NOTIFICATIONS") {
            SettingsCard {
                SettingsRow(
                    icon = Icons.Outlined.Notifications,
                    title = "Enable Notifications",
                    trailing = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && notifPermission != null && !notifPermission.status.isGranted) {
                                    notifPermission.launchPermissionRequest()
                                } else {
                                    viewModel.setNotificationsEnabled(enabled)
                                }
                            },
                            thumbContent = if (notificationsEnabled) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                        tint = Primary
                                    )
                                }
                            } else null,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Primary,
                                checkedBorderColor = Primary
                            )
                        )
                    }
                )
                HorizontalDivider(color = DividerColor)
                SettingsRow(
                    icon = Icons.Outlined.Schedule,
                    title = "Daily Alert Time",
                    onClick = { showTimePicker = true },
                    trailing = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SettingsValueText(String.format("%02d:%02d", alertHour, alertMinute))
                            Spacer(Modifier.width(4.dp))
                            ChevronIcon(tint = ThemeText.copy(alpha = 0.4f))
                        }
                    }
                )
                HorizontalDivider(color = DividerColor)
                SettingsRow(
                    icon = Icons.Outlined.NotificationsActive,
                    title = "Advance Warning",
                    onClick = { showDaysPicker = true },
                    trailing = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SettingsValueText(
                                if (advanceDays == 1) "1 day before" else "$advanceDays days before"
                            )
                            Spacer(Modifier.width(4.dp))
                            ChevronIcon(tint = ThemeText.copy(alpha = 0.4f))
                        }
                    }
                )
            }
        }

        SettingsSection(title = "STATISTICS & DATA") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (totalCount == 0) {
                    SettingsCard {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No data available yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ThemeText.copy(alpha = 0.4f)
                            )
                        }
                    }
                } else {
                    val consumedPercent = (consumedCount * 100f / totalCount).toInt()
                    FoodHabitCard(
                        consumedCount = consumedCount,
                        thrownCount = thrownCount,
                        consumedPercent = consumedPercent
                    )
                }

                SettingsCard {
                    SettingsRow(
                        icon = Icons.Outlined.FileDownload,
                        title = "Export Data",
                        onClick = {
                            scope.launch {
                                val file = viewModel.exportCsvFile()
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    putExtra(Intent.EXTRA_SUBJECT, "Shelfy – Product Export")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Export to…"))
                            }
                        },
                        trailing = { ChevronIcon(tint = ThemeText.copy(alpha = 0.4f)) }
                    )
                }

                SettingsCard {
                    SettingsRow(
                        icon = Icons.Outlined.Delete,
                        title = "Clear All Data",
                        onClick = { showClearConfirm = true },
                        contentColor = Error,
                        trailing = { ChevronIcon(tint = Error) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsValueText(value: String) {
    Text(
        text = value,
        color = ThemeText.copy(alpha = 0.55f),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ChevronIcon(tint: Color) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}
