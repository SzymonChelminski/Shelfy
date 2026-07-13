package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shelfy.model.FoodCategories
import com.example.shelfy.model.FoodItem
import com.example.shelfy.ui.theme.Error
import com.example.shelfy.ui.theme.Primary
import com.example.shelfy.ui.theme.Warning

@Composable
fun FoodItemCard(
    item: FoodItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val badgeColor = when {
        item.daysLeft <= 0 -> Error
        item.daysLeft < 3 -> Color(0xFFEA580C)
        item.daysLeft < 7 -> Warning
        else -> Primary
    }

    val catColor = if (item.category.isNotEmpty()) FoodCategories.colorFor(item.category) else Primary

    Card(
        onClick = onClick,
        modifier = modifier
            .width(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                ProductImage(
                    imageUrl = item.imageUrl,
                    tint = catColor,
                    contentDescription = item.name,
                    iconSize = 40.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = item.expirationLabel,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                Text(text = item.category, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
