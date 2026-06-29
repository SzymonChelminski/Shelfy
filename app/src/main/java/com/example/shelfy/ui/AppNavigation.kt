package com.example.shelfy.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shelfy.di.DatabaseModule
import com.example.shelfy.model.FoodItem
import com.example.shelfy.model.PendingProduct
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.shelfy.ui.components.AddProductDialog
import com.example.shelfy.ui.components.AddProductScreen
import com.example.shelfy.ui.components.DashboardScreen
import com.example.shelfy.ui.components.InventoryScreen
import com.example.shelfy.ui.components.ProductDetailsScreen
import com.example.shelfy.ui.components.SettingsScreen
import com.example.shelfy.ui.components.ShelfyFAB
import com.example.shelfy.ui.components.ShoppingScreen
import com.example.shelfy.ui.viewmodel.ScannerViewModel
import com.example.shelfy.ui.viewmodel.ShoppingViewModel

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
    val viewModel: ScannerViewModel = viewModel(
        factory = ScannerViewModel.factory(DatabaseModule.repository)
    )
    val shoppingViewModel: ShoppingViewModel = viewModel(
        factory = ShoppingViewModel.factory(DatabaseModule.shoppingRepository)
    )
    val savedProducts by viewModel.savedProducts.collectAsState()
    val foodItems = remember(savedProducts) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        savedProducts.map { entity ->
            val daysLeft = ((entity.expiryDateMillis - System.currentTimeMillis()) / (1000L * 60 * 60 * 24)).toInt()
            FoodItem(
                id = entity.id.toInt(),
                name = entity.name,
                category = entity.brand,
                expirationLabel = dateFormat.format(Date(entity.expiryDateMillis)),
                imageUrl = entity.imageUrl,
                daysLeft = daysLeft
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
            composable(Routes.DASHBOARD) {
                MainLayout(
                    navController = navController,
                    fab = { ShelfyFAB(onClick = { navController.navigate(Routes.ADD_PRODUCT) }) }
                ) {
                    DashboardScreen(
                        items = foodItems,
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
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.INVENTORY) {
                MainLayout(navController = navController) {
                    InventoryScreen(
                        items = foodItems,
                        onProductClick = { itemId -> navController.navigate(Routes.productDetails(itemId)) }
                    )
                }
            }

            composable(Routes.SHOPPING) {
                MainLayout(navController = navController) {
                    ShoppingScreen(viewModel = shoppingViewModel)
                }
            }

            composable(
                route = Routes.PRODUCT_DETAILS,
                arguments = listOf(navArgument(Routes.PRODUCT_DETAILS_ARG) { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt(Routes.PRODUCT_DETAILS_ARG)
                val item = foodItems.firstOrNull { it.id == itemId }

                if (item == null) {
                    navController.popBackStack()
                    return@composable
                }

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
                onConfirm = { name, brand, expiryDateMillis ->
                    pendingProduct?.let { product ->
                        viewModel.saveProduct(
                            barcode = product.barcode,
                            name = name,
                            brand = brand,
                            imageUrl = product.imageUrl,
                            nutriscoreGrade = product.nutriscoreGrade,
                            expiryDateMillis = expiryDateMillis
                        )
                    }
                    pendingProduct = null
                }
            )
        }
    }
}
