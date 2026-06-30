package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodItem
import com.example.shelfy.ui.theme.Text as ThemeText

private const val ALL_CATEGORIES = "All"

@Composable
fun InventoryScreen(
    items: List<FoodItem>,
    modifier: Modifier = Modifier,
    onProductClick: (Int) -> Unit
) {
    val categories = remember(items) {
        listOf(ALL_CATEGORIES) + items.map { it.category }.distinct()
    }

    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ALL_CATEGORIES) }

    val filteredItems = items
        .filter { item ->
            val matchesCategory = selectedCategory == ALL_CATEGORIES || item.category == selectedCategory
            val matchesQuery = item.name.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
        .sortedWith(compareBy({ it.category }, { it.daysLeft }))

    val isFiltering = query.isNotEmpty() || selectedCategory != ALL_CATEGORIES

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Inventory",
            color = ThemeText,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShelfySearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = "Search inventory..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        InventoryFilterChips(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isFiltering) {
                    EmptyStateView(
                        icon = Icons.Outlined.SearchOff,
                        title = "No matching products",
                        subtitle = "Try a different search or category"
                    )
                } else {
                    EmptyStateView(
                        icon = Icons.Outlined.Inventory2,
                        title = "Inventory is empty",
                        subtitle = "Scan a product barcode to add it"
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    InventoryItemCard(
                        item = item,
                        onClick = { onProductClick(item.id) }
                    )
                }
            }
        }
    }
}
