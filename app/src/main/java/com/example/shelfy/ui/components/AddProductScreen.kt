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
import com.example.shelfy.data.remote.dto.OpenFoodProductDto
import com.example.shelfy.data.repository.ProductRepository
import com.example.shelfy.model.PendingProduct
import com.example.shelfy.ui.theme.Background
import com.example.shelfy.ui.theme.Text as TextColor

@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onProductScanned: (PendingProduct) -> Unit
) {
    var scannedBarcode by remember { mutableStateOf("") }
    var fetchedProduct by remember { mutableStateOf<OpenFoodProductDto?>(null) }
    var isFetchComplete by remember { mutableStateOf(false) }
    var isAnimationComplete by remember { mutableStateOf(false) }

    LaunchedEffect(scannedBarcode) {
        if (scannedBarcode.isNotEmpty()) {
            Log.d("BARCODE_SCAN", "Fetching: $scannedBarcode")
            fetchedProduct = ProductRepository.getProduct(scannedBarcode)
            Log.d("BARCODE_SCAN", "Result: $fetchedProduct")
            isFetchComplete = true
        }
    }

    LaunchedEffect(isFetchComplete, isAnimationComplete) {
        if (isFetchComplete && isAnimationComplete) {
            onProductScanned(
                PendingProduct(
                    barcode = scannedBarcode,
                    name = fetchedProduct?.productName,
                    brand = fetchedProduct?.brands,
                    imageUrl = fetchedProduct?.imageUrl,
                    nutriscoreGrade = fetchedProduct?.nutriscoreGrade
                )
            )
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
                    if (scannedBarcode.isEmpty()) scannedBarcode = barcode
                }
            )
            if (scannedBarcode.isNotEmpty()) {
                ScanSuccessOverlay(
                    onAnimationComplete = { isAnimationComplete = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(56.dp))

        ManualEntryCard(
            onManualAddClick = {
                onProductScanned(
                    PendingProduct(
                        barcode = "manual_${System.currentTimeMillis()}",
                        name = null,
                        brand = null,
                        imageUrl = null,
                        nutriscoreGrade = null
                    )
                )
            }
        )
    }
}
