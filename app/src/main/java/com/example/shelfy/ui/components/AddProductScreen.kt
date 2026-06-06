package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shelfy.ui.theme.Background
import com.example.shelfy.ui.theme.Text as TextColor

@Composable
fun AddProductScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        // Ustawia wszystkie elementy idealnie na środku ekranu w pionie
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Scan barcode",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Place the product inside the frame",
            style = MaterialTheme.typography.bodyMedium,
            color = TextColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(56.dp))

        Box(
            modifier = Modifier.size(250.dp),
            contentAlignment = Alignment.Center
        ) {
            CameraPermissionWrapper()
        }

        Spacer(modifier = Modifier.height(56.dp))

        ManualEntryCard(
            onManualAddClick = { }
        )
    }
}