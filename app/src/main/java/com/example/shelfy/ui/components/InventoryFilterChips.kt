package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodCategories
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.Surface
import com.example.shelfy.ui.theme.Text as ThemeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryFilterChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val chipColor = if (category == "All") Primary else FoodCategories.colorFor(category)
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(text = category) },
                shape = RoundedCornerShape(50),
                border = null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = chipColor.copy(alpha = 0.15f),
                    labelColor = chipColor,
                    selectedContainerColor = chipColor,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
