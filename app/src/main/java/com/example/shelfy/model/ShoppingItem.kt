package com.example.shelfy.model

data class ShoppingItem(
    val id: Int,
    val name: String,
    val category: String,
    val quantity: String,
    val isCompleted: Boolean
)
