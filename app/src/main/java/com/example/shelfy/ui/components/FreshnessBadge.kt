package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shelfy.ui.theme.Error
import com.example.shelfy.ui.theme.Success
import com.example.shelfy.ui.theme.Warning

@Composable
fun FreshnessBadge(
    daysLeft: Int,
    modifier: Modifier = Modifier
) {
    val color: Color
    val icon: ImageVector
    val label: String

    when {
        daysLeft <= 0 -> {
            color = Error
            icon = Icons.Outlined.WarningAmber
            label = "Expired"
        }
        daysLeft <= 1 -> {
            color = Error
            icon = Icons.Outlined.WarningAmber
            label = "Expiring soon"
        }
        daysLeft <= 3 -> {
            color = Warning
            icon = Icons.Outlined.WarningAmber
            label = "Consume soon"
        }
        else -> {
            color = Success
            icon = Icons.Outlined.CheckCircle
            label = "Fresh"
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
