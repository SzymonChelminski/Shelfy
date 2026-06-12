package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelfy.model.FoodItem

@Composable
fun FreshnessStatusCard(item: FoodItem) {
    val progress = item.getFreshnessProgress()
    val status = item.getFreshnessStatus()
    val isExpired = item.daysLeft <= 0
    val progressColor = when (status) {
        "Expired" -> Color(0xFFDC2626)
        "Eat immediately" -> Color(0xFFF59E0B)
        "Eat soon" -> Color(0xFF3B82F6)
        else -> Color(0xFF10B981)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xE0EEEEEE), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = progressColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status,
                        fontWeight = FontWeight.Bold,
                        color = if (isExpired) progressColor else Color(0xFF111827),
                        fontSize = 16.sp
                    )
                }
                if (!isExpired) {
                    Text(
                        text = "${(progress * 100).toInt()}% left",
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp
                    )
                }
            }

            if (!isExpired) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFE5E7EB), RoundedCornerShape(50))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progress)
                            .fillMaxHeight()
                            .background(progressColor, RoundedCornerShape(50))
                    )
                }
            }
        }
    }
}