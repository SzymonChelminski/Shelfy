package com.example.shelfy.model

import com.example.shelfy.R

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val expirationLabel: String,
    val imageResourceId: Int
) {
    companion object {
        val mockFoodList = listOf(
            FoodItem(1, "2% Milk", "Dairy", "Today", R.drawable.milk),
            FoodItem(2, "Asparagus", "Vegetables", "1 day left", R.drawable.asparagus),
            FoodItem(3, "Strawberries", "Fruits", "2 days left", R.drawable.strawberries),
            FoodItem(4, "Chicken Breast", "Meat", "3 days left", R.drawable.chicken)
        )
    }
}