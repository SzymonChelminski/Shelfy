package com.example.shelfy.model

import com.example.shelfy.R

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val expirationLabel: String,
    val imageResourceId: Int,
    val daysLeft: Int,
) {

    fun getFreshnessProgress(): Float {
        return when {
            daysLeft >= 7 -> 1.0f
            daysLeft <= 0 -> 0.0f
            else -> daysLeft.toFloat() / 7f
        }
    }

    fun getFreshnessStatus(): String {
        return when {
            daysLeft <= 0 -> "Expired"
            daysLeft < 3 -> "Eat immediately"
            daysLeft < 7 -> "Eat soon"
            else -> "Fresh"
        }
    }

    companion object {
        val mockFoodList = listOf(
            FoodItem(1, "2% Milk", "Dairy", "Today", R.drawable.milk, 0),
            FoodItem(2, "Asparagus", "Vegetables", "1 day left", R.drawable.asparagus, 1),
            FoodItem(3, "Strawberries", "Fruits", "2 days left", R.drawable.strawberries, 2),
            FoodItem(4, "Chicken Breast", "Meat", "3 days left", R.drawable.chicken, 3)
        )
    }
}