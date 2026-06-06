package com.example.shelfy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shelfy.ui.components.ShelfyFAB
import com.example.shelfy.ui.components.ShelfyNavigationBar
import com.example.shelfy.ui.components.ShelfyTopBar


@Composable
fun MainLayout(
    fab: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { ShelfyTopBar() },
        bottomBar = { ShelfyNavigationBar() },
        floatingActionButton = fab
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}
