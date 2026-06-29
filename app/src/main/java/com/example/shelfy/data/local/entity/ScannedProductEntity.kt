package com.example.shelfy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_products")
data class ScannedProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val barcode: String,
    val name: String,
    val brand: String,
    val imageUrl: String?,
    val nutriscoreGrade: String?,
    val expiryDateMillis: Long,
    val dateAddedMillis: Long = System.currentTimeMillis()
)
