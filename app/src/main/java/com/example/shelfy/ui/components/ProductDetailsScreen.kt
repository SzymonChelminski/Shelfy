package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodItem

@Composable
fun ProductDetailsScreen(
    item: FoodItem,
    onConsume: () -> Unit,
    onThrowAway: () -> Unit,
    onEdit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            ProductHeader(item = item)
            BackButton(
                onClick = onBack,
                tint = Color.White,
                backgroundColor = Color.Black.copy(alpha = 0.35f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FreshnessStatusCard(item = item)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoCard(
                    icon = Icons.Default.DateRange,
                    title = "Expires in",
                    value = item.expirationLabel,
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    icon = Icons.Default.ShoppingCart,
                    title = "Quantity",
                    value = "1 Bundle (~450g)",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            DetailsActionButtons(
                onConsume = onConsume,
                onThrowAway = onThrowAway,
                onEdit = onEdit
            )
        }
    }
}