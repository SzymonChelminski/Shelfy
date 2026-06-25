package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodItem
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.Text

private enum class RipenessSort(val label: String) {
    MOST_RIPE("Most ripe"),
    LEAST_RIPE("Least ripe");

    fun toggled(): RipenessSort = if (this == MOST_RIPE) LEAST_RIPE else MOST_RIPE
}

@Composable
fun DashboardScreen(
    items: List<FoodItem>,
    modifier: Modifier = Modifier,
    onProductClick: (Int) -> Unit,
    onSeeAllProducts: () -> Unit,
    onFabClick: () -> Unit = {}
) {
    var sortOrder by remember { mutableStateOf(RipenessSort.MOST_RIPE) }

    val eatSoonItems = items.sortedBy { it.daysLeft }
    val sortedItems = when (sortOrder) {
        RipenessSort.MOST_RIPE -> items.sortedBy { it.daysLeft }
        RipenessSort.LEAST_RIPE -> items.sortedByDescending { it.daysLeft }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DashboardSection(
            title = "Eat soon",
            buttonText = "See all",
            buttonTextColor = Primary,
            onSeeAllClick = onSeeAllProducts
        ) {
            if (eatSoonItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                        .height(160.dp)
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        icon = Icons.Outlined.Schedule,
                        title = "Nothing expiring soon",
                        subtitle = "Add products to track their freshness"
                    )
                }
            } else {
                LazyRow {
                    items(eatSoonItems) { foodItem ->
                        FoodItemCard(
                            item = foodItem,
                            onClick = { onProductClick(foodItem.id) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        DashboardSection(
            title = "Recently added",
            buttonText = sortOrder.label,
            buttonTextColor = Text,
            icon = {
                Icon(
                    Icons.Filled.SwapVert,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            onSeeAllClick = { sortOrder = sortOrder.toggled() },
        ) {
            if (sortedItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                        .height(160.dp)
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        icon = Icons.Outlined.Inventory2,
                        title = "No products yet",
                        subtitle = "Tap + to scan your first product"
                    )
                }
            } else {
                val totalItems = sortedItems.size
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    sortedItems.take(3).forEach { foodItem ->
                        FoodListCard(
                            item = foodItem,
                            onClick = { onProductClick(foodItem.id) }
                        )
                    }
                    if (totalItems > 3) {
                        SeeAllProductsCard(
                            totalCount = totalItems,
                            onClick = onSeeAllProducts
                        )
                    }
                }
            }
        }
    }
}
