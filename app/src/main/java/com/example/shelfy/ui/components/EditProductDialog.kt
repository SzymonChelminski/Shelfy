package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shelfy.data.local.entity.ScannedProductEntity
import com.example.shelfy.model.FoodCategories
import com.example.shelfy.ui.theme.Primary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.shelfy.ui.theme.Text as TextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    entity: ScannedProductEntity,
    onDismiss: () -> Unit,
    onConfirm: (name: String, brand: String, expiryDateMillis: Long, quantity: String, category: String) -> Unit
) {
    var name by remember { mutableStateOf(entity.name) }
    var brand by remember { mutableStateOf(entity.brand) }
    var quantity by remember { mutableStateOf(entity.quantity) }
    var selectedCategory by remember { mutableStateOf(entity.category) }
    var submitted by remember { mutableStateOf(false) }
    val sdf = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
    }
    var expiryDate by remember { mutableStateOf(sdf.format(Date(entity.expiryDateMillis))) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = entity.expiryDateMillis)

    val nameError = if (submitted) when {
        name.trim().length < 2 -> "At least 2 characters required"
        name.trim().length > 50 -> "Too long (max 50 characters)"
        else -> null
    } else null

    val quantityError = if (submitted && quantity.isNotBlank() && quantity.trim().isEmpty()) {
        "Invalid quantity"
    } else null

    val categoryError = if (submitted && selectedCategory.isEmpty()) "Please select a category" else null

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        expiryDate = sdf.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Edit product",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { if (it.length <= 50) name = it },
                        label = { Text("Product name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = nameError != null
                    )
                    if (nameError != null) {
                        Text(nameError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }

                OutlinedTextField(
                    value = brand,
                    onValueChange = { if (it.length <= 30) brand = it },
                    label = { Text("Brand") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { if (it.length <= 20) quantity = it },
                        label = { Text("Quantity") },
                        placeholder = { Text("e.g. 500g, 2 pcs") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = quantityError != null
                    )
                    if (quantityError != null) {
                        Text(quantityError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (categoryError != null) MaterialTheme.colorScheme.error else TextColor.copy(alpha = 0.6f)
                    )
                    val chipScrollState = rememberScrollState()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawWithContent {
                                drawContent()
                                if (chipScrollState.canScrollBackward) {
                                    drawRect(
                                        brush = Brush.horizontalGradient(
                                            0f to Color.White,
                                            0.25f to Color.Transparent
                                        )
                                    )
                                }
                                if (chipScrollState.canScrollForward) {
                                    drawRect(
                                        brush = Brush.horizontalGradient(
                                            0.75f to Color.Transparent,
                                            1f to Color.White
                                        )
                                    )
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.horizontalScroll(chipScrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FoodCategories.all.forEach { cat ->
                                val catColor = FoodCategories.colorFor(cat)
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat) },
                                    shape = RoundedCornerShape(50),
                                    border = null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = catColor.copy(alpha = 0.15f),
                                        labelColor = catColor,
                                        selectedContainerColor = catColor,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                    if (categoryError != null) {
                        Text(categoryError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }

                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Expiry date") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Pick date", tint = Primary)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextColor.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            submitted = true
                            val isNameValid = name.trim().length in 2..50
                            val isQuantityValid = quantity.isBlank() || quantity.trim().isNotEmpty()
                            val isCategoryValid = selectedCategory.isNotEmpty()
                            if (isNameValid && isQuantityValid && isCategoryValid) {
                                onConfirm(
                                    name.trim(),
                                    brand.trim(),
                                    datePickerState.selectedDateMillis ?: entity.expiryDateMillis,
                                    quantity.trim(),
                                    selectedCategory
                                )
                            }
                        },
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}
