package com.example.shelfy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shelfy.ui.AppNavigation
import com.example.shelfy.ui.theme.ShelfyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShelfyTheme {
                AppNavigation()
            }
        }
    }
}