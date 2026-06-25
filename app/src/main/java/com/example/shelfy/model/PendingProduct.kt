package com.example.shelfy.model

data class PendingProduct(
    val barcode: String,
    val name: String?,
    val brand: String?,
    val imageUrl: String?,
    val nutriscoreGrade: String?
)
