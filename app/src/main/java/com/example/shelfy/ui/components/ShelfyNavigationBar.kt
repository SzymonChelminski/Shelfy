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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shelfy.ui.Routes
import com.example.shelfy.ui.theme.Text as TextColor

@Composable
fun ShelfyNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf("Home", "Inventory", "Details")
    val routes = listOf(Routes.DASHBOARD, "inventory", Routes.PRODUCT_DETAILS)
    val icons = listOf(Icons.Outlined.SpaceDashboard, Icons.Outlined.Kitchen, Icons.Outlined.Info)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = currentRoute == routes[index],
                onClick = {
                    val targetRoute = routes[index]
                    if (currentRoute != targetRoute) {
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        modifier = Modifier.padding(4.dp).size(32.dp)
                    )
                },
                label = { Text(text = label, fontSize = 14.sp) },
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