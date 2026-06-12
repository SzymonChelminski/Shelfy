package com.example.shelfy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.shelfy.ui.components.ShelfyNavigationBar
import com.example.shelfy.ui.components.ShelfyTopBar

@Composable
fun MainLayout(
    navController: NavController,
    fab: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { ShelfyTopBar(onSettingsClick = { navController.navigate(Routes.SETTINGS) }) },
        bottomBar = { ShelfyNavigationBar(navController = navController) },
        floatingActionButton = fab
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}