package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.Text

private enum class RipenessSort(val label: String) {
    MOST_RIPE("Most ripe"),
    LEAST_RIPE("Least ripe");

    fun toggled(): RipenessSort = if (this == MOST_RIPE) LEAST_RIPE else MOST_RIPE
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Int) -> Unit,
    onSeeAllProducts: () -> Unit,
    onFabClick: () -> Unit = {}
) {
    var sortOrder by remember { mutableStateOf(RipenessSort.MOST_RIPE) }

    val eatSoonItems = FoodItem.mockFoodList.sortedBy { it.daysLeft }
    val sortedItems = when (sortOrder) {
        RipenessSort.MOST_RIPE -> FoodItem.mockFoodList.sortedBy { it.daysLeft }
        RipenessSort.LEAST_RIPE -> FoodItem.mockFoodList.sortedByDescending { it.daysLeft }
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
            LazyRow {
                items(eatSoonItems) { foodItem ->
                    FoodItemCard(
                        item = foodItem,
                        onClick = { onProductClick(foodItem.id) }
                    )
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
