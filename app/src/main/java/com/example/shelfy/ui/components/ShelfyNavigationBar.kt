package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelfy.ui.theme.Text as TextColor
import com.example.shelfy.ui.theme.Secondary

@Composable
fun ShelfyNavigationBar() {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf("Home", "Inventory", "Details")
    val icons = listOf(Icons.Outlined.SpaceDashboard, Icons.Outlined.Kitchen, Icons.Outlined.Info)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        modifier = Modifier.padding(4.dp).size(32.dp)
                    )
                },
                label = { Text(text= label, fontSize = 14.sp) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f),
                    selectedIconColor = Color.Black,
                    unselectedIconColor = TextColor,
                    selectedTextColor = TextColor,
                    unselectedTextColor = TextColor
                )
            )
        }
    }
}