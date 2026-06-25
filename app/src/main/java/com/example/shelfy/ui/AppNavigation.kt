package com.example.shelfy.ui

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shelfy.model.FoodItem
import com.example.shelfy.model.PendingProduct
import com.example.shelfy.ui.components.AddProductDialog
import com.example.shelfy.ui.components.AddProductScreen
import com.example.shelfy.ui.components.DashboardScreen
import com.example.shelfy.ui.components.InventoryScreen
import com.example.shelfy.ui.components.ProductDetailsScreen
import com.example.shelfy.ui.components.SettingsScreen
import com.example.shelfy.ui.components.ShelfyFAB
import com.example.shelfy.ui.components.ShoppingScreen

object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD_PRODUCT = "add_product"
    const val INVENTORY = "inventory"
    const val SHOPPING = "shopping"
    const val SETTINGS = "settings"
    const val PRODUCT_DETAILS_ARG = "itemId"
    const val PRODUCT_DETAILS = "product_details/{$PRODUCT_DETAILS_ARG}"

    fun productDetails(itemId: Int) = "product_details/$itemId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var pendingProduct by remember { mutableStateOf<PendingProduct?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
            composable(Routes.DASHBOARD) {
                MainLayout(
                    navController = navController,
                    fab = { ShelfyFAB(onClick = { navController.navigate(Routes.ADD_PRODUCT) }) }
                ) {
                    DashboardScreen(
                        onFabClick = { navController.navigate(Routes.ADD_PRODUCT) },
                        onProductClick = { itemId -> navController.navigate(Routes.productDetails(itemId)) },
                        onSeeAllProducts = { navController.navigate(Routes.INVENTORY) }
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
                    onBack = { navController.popBackStack() },
                    onProductScanned = { product ->
                        pendingProduct = product
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Routes.SETTINGS,
                enterTransition = { slideInHorizontally(animationSpec = tween(300)) { it } },
                exitTransition = { slideOutHorizontally(animationSpec = tween(300)) { -it } },
                popEnterTransition = { slideInHorizontally(animationSpec = tween(300)) { -it } },
                popExitTransition = { slideOutHorizontally(animationSpec = tween(300)) { it } }
            ) {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.INVENTORY) {
                MainLayout(navController = navController) {
                    InventoryScreen(
                        onProductClick = { itemId -> navController.navigate(Routes.productDetails(itemId)) }
                    )
                }
            }

            composable(Routes.SHOPPING) {
                MainLayout(navController = navController) {
                    ShoppingScreen()
                }
            }

            composable(
                route = Routes.PRODUCT_DETAILS,
                arguments = listOf(navArgument(Routes.PRODUCT_DETAILS_ARG) { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt(Routes.PRODUCT_DETAILS_ARG)
                val item = FoodItem.mockFoodList.firstOrNull { it.id == itemId }
                    ?: FoodItem.mockFoodList.first()

                MainLayout(navController = navController) {
                    ProductDetailsScreen(
                        item = item,
                        onConsume = { navController.popBackStack() },
                        onThrowAway = { navController.popBackStack() },
                        onEdit = { },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }

        pendingProduct?.let { product ->
            AddProductDialog(
                product = product,
                onDismiss = { pendingProduct = null },
                onConfirm = { name, brand, expiryDate ->
                    Log.d("ADD_PRODUCT", "name=$name  brand=$brand  expiry=$expiryDate")
                    pendingProduct = null
                }
            )
        }
    }
}
