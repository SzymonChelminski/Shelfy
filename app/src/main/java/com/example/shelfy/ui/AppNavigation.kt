package com.example.shelfy.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shelfy.ui.components.AddProductScreen
import com.example.shelfy.ui.components.DashboardScreen
import com.example.shelfy.ui.components.ShelfyFAB

object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD_PRODUCT = "add_product"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) {
            MainLayout(
                fab = { ShelfyFAB(onClick = { navController.navigate(Routes.ADD_PRODUCT) }) }
            ) {
                DashboardScreen(onFabClick = { navController.navigate(Routes.ADD_PRODUCT) })
            }
        }
        composable(
            route = Routes.ADD_PRODUCT,
            enterTransition = { slideInHorizontally(animationSpec = tween(300)) { it } },
            exitTransition = { slideOutHorizontally(animationSpec = tween(300)) { -it } },
            popEnterTransition = { slideInHorizontally(animationSpec = tween(300)) { -it } },
            popExitTransition = { slideOutHorizontally(animationSpec = tween(300)) { it } }
        ) {
            AddProductScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}