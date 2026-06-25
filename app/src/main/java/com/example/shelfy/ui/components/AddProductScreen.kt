package com.example.shelfy.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shelfy.data.repository.ProductRepository
import com.example.shelfy.ui.theme.Background
import com.example.shelfy.ui.theme.Text as TextColor

@Composable
fun AddProductScreen(onBack: () -> Unit) {
    var scannedBarcode by remember { mutableStateOf("") }

    LaunchedEffect(scannedBarcode) {
        if (scannedBarcode.isNotEmpty()) {
            Log.d("BARCODE_SCAN", "Fetching product for: $scannedBarcode")
            val product = ProductRepository.getProduct(scannedBarcode)
            if (product != null) {
                Log.d("BARCODE_SCAN", "Product: $product")
            } else {
                Log.d("BARCODE_SCAN", "Not found for: $scannedBarcode")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
            CameraPermissionWrapper(
                onBarcodeScanned = { barcode ->
                    if (scannedBarcode.isEmpty()) {
                        scannedBarcode = barcode
                    }
                }
            )
            if (scannedBarcode.isNotEmpty()) {
                ScanSuccessOverlay(onAnimationComplete = onBack)
            }
        }

        Spacer(modifier = Modifier.height(56.dp))

        ManualEntryCard(
            onManualAddClick = { }
        )
    }
}
