package com.example.shelfy.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shelfy.model.FoodItem
import com.example.shelfy.ui.components.AddProductScreen
import com.example.shelfy.ui.components.DashboardScreen
import com.example.shelfy.ui.components.ProductDetailsScreen
import com.example.shelfy.ui.components.ShelfyFAB

object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD_PRODUCT = "add_product"
    const val PRODUCT_DETAILS = "product_details"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) {
            MainLayout(
                navController = navController,
                fab = { ShelfyFAB(onClick = { navController.navigate(Routes.ADD_PRODUCT) }) }
            ) {
                DashboardScreen(
                    onFabClick = { navController.navigate(Routes.ADD_PRODUCT) },
                    onProductClick = { navController.navigate(Routes.PRODUCT_DETAILS) }
                )
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

        composable(Routes.PRODUCT_DETAILS) {
            MainLayout(
                navController = navController
            ) {
                ProductDetailsScreen(
                    item = FoodItem.mockFoodList[1],
                    onConsume = { navController.popBackStack() },
                    onThrowAway = { navController.popBackStack() },
                    onEdit = {  }
                )
            }
        }
    }
}