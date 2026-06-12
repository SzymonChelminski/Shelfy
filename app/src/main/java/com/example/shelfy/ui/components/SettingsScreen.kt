package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shelfy.ui.theme.Error
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.Text as ThemeText

private val SettingsBackground = Brush.verticalGradient(
    colors = listOf(Color(0xFFE3F3E7), Color(0xFFF6FBF7))
)

private val DividerColor = Color(0xFFEFEFEF)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onExportData: () -> Unit = {},
    onClearData: () -> Unit = {},
    onDefaultSortingClick: () -> Unit = {}
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

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
                            onCheckedChange = { notificationsEnabled = it },
                            thumbContent = if (notificationsEnabled) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                        tint = Primary
                                    )
                                }
                            } else {
                                null
                            },
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
                    trailing = { SettingsValueText("18:00") }
                )
                HorizontalDivider(color = DividerColor)
                SettingsRow(
                    icon = Icons.Outlined.NotificationsActive,
                    title = "Advance Warning",
                    trailing = { SettingsValueText("2 days before") }
                )
            }
        }

        SettingsSection(title = "PREFERENCES") {
            SettingsCard {
                SettingsRow(
                    icon = Icons.AutoMirrored.Filled.Sort,
                    title = "Default Sorting",
                    onClick = onDefaultSortingClick,
                    trailing = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Expiring first",
                                color = Primary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            ChevronIcon(tint = Primary)
                        }
                    }
                )
            }
        }

        SettingsSection(title = "STATISTICS & DATA") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                FoodHabitCard(consumedPercent = 85)

                SettingsCard {
                    SettingsRow(
                        icon = Icons.Outlined.FileDownload,
                        title = "Export Data",
                        onClick = onExportData,
                        trailing = { ChevronIcon(tint = ThemeText.copy(alpha = 0.4f)) }
                    )
                }

                SettingsCard {
                    SettingsRow(
                        icon = Icons.Outlined.Delete,
                        title = "Clear All Data",
                        contentColor = Error,
                        onClick = onClearData,
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
