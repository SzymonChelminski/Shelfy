package com.example.shelfy.model

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val expirationLabel: String,
    val imageUrl: String?,
    val daysLeft: Int,
    val quantity: String = "",
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
}
