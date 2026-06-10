package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F9F4)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF111827),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color(0xFF6B7280),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = Color(0xFF111827),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}