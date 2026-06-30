package com.example.shelfy.model

import androidx.compose.ui.graphics.Color

object FoodCategories {
    val all = listOf(
        "Dairy", "Meat & Fish", "Vegetables", "Fruits",
        "Bakery", "Beverages", "Water", "Snacks", "Frozen", "Condiments", "Other"
    )

    private val colorMap = mapOf(
        "Dairy"       to Color(0xFF3B82F6),
        "Meat & Fish" to Color(0xFFEF4444),
        "Vegetables"  to Color(0xFF22C55E),
        "Fruits"      to Color(0xFFF97316),
        "Bakery"      to Color(0xFFD97706),
        "Beverages"   to Color(0xFF0EA5E9),
        "Water"       to Color(0xFF7DD3FC),
        "Snacks"      to Color(0xFF8B5CF6),
        "Frozen"      to Color(0xFF06B6D4),
        "Condiments"  to Color(0xFFCA8A04),
        "Other"       to Color(0xFF6B7280)
    )

    fun colorFor(category: String): Color = colorMap[category] ?: Color(0xFF6B7280)
}
