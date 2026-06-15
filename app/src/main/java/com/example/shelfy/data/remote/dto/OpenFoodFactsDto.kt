package com.example.shelfy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    val status: Int,
    val code: String?,
    val product: OpenFoodProductDto?
)

data class OpenFoodProductDto(
    @SerializedName("product_name") val productName: String?,
    val brands: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("nutriscore_grade") val nutriscoreGrade: String?
)