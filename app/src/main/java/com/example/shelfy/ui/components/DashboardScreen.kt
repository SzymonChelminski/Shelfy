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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.Text

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
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
            onSeeAllClick = { }
        ) {
            LazyRow {
                items(FoodItem.mockFoodList) { foodItem ->
                    FoodItemCard(item = foodItem)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        DashboardSection(
            title = "Recently added",
            buttonText = "Filter",
            buttonTextColor = Text,
            icon = {
                Icon(
                    Icons.Filled.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            onSeeAllClick = { },
        ) {
        }
    }
}