package com.example.shelfy.model

data class ShoppingItem(
    val id: Int,
    val name: String,
    val category: String,
    val quantity: String,
    val isCompleted: Boolean
) {
    companion object {
        val mockShoppingList = listOf(
            ShoppingItem(1, "Organic Avocados", "Produce", "3 pcs", false),
            ShoppingItem(2, "Almond Milk", "Dairy", "1 L", false),
            ShoppingItem(3, "Sourdough Bread", "Bakery", "1 loaf", false),
            ShoppingItem(4, "Cherry Tomatoes", "Produce", "1 box", true),
            ShoppingItem(5, "Free Range Eggs", "Dairy", "12 pack", true)
        )
    }
}
