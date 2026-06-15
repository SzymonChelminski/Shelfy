package com.example.shelfy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.example.shelfy.data.repository.ProductRepository
import com.example.shelfy.ui.AppNavigation
import com.example.shelfy.ui.theme.ShelfyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShelfyTheme {
                LaunchedEffect(Unit) {
                    val product = ProductRepository.getProduct("5449000000996")

                    if (product != null) {
                        Log.d("API_TEST", "Returned obj: $product")
                    } else {
                        Log.d("API_TEST", "Error")
                    }
                }

                AppNavigation()
            }
        }
    }
}