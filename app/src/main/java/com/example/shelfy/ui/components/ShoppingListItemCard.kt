package com.example.shelfy.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.ShoppingItem
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.PrimaryDark
import com.example.shelfy.ui.theme.Surface
import com.example.shelfy.ui.theme.Text as ThemeText

@Composable
fun ShoppingListItemCard(
    item: ShoppingItem,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onToggle,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) Color(0xFFE9EFEA) else Surface
        ),
        border = if (item.isCompleted) null else BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckCircle(checked = item.isCompleted)

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                color = if (item.isCompleted) ThemeText.copy(alpha = 0.45f) else ThemeText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
            )

            if (!item.isCompleted) {
                CategoryBadge(category = item.category)
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = item.quantity,
                color = if (item.isCompleted) ThemeText.copy(alpha = 0.4f) else ThemeText.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
            )
        }
    }
}

@Composable
private fun CheckCircle(checked: Boolean) {
    if (checked) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .border(2.dp, ThemeText.copy(alpha = 0.25f), CircleShape)
        )
    }
}

@Composable
private fun CategoryBadge(category: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Primary.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = category,
            color = PrimaryDark,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}
