package com.example.shelfy.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shelfy.ui.components.ShelfyTopBar


@Composable
fun MainLayout(
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { ShelfyTopBar() },
        bottomBar = {}
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}
