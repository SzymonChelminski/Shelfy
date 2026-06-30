package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.ShoppingCart
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shelfy.ui.Routes
import com.example.shelfy.ui.theme.Text as TextColor

private data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun ShelfyNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem("Home", Routes.DASHBOARD, Icons.Outlined.SpaceDashboard),
        BottomNavItem("Inventory", Routes.INVENTORY, Icons.Outlined.Kitchen),
        BottomNavItem("Shopping", Routes.SHOPPING, Icons.Outlined.ShoppingCart)
    )

    NavigationBar(
        containerColor = Color.White,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute == item.route) {
                        return@NavigationBarItem
                    }
                    if (item.route == Routes.DASHBOARD) {
                        val poppedToDashboard = navController.popBackStack(Routes.DASHBOARD, false)
                        if (!poppedToDashboard) {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.DASHBOARD) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.padding(4.dp).size(32.dp)
                    )
                },
                label = { Text(text = item.label, fontSize = 14.sp) },
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
