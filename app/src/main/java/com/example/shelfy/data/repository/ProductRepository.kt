package com.example.shelfy.data.repository

import com.example.shelfy.data.remote.dto.OpenFoodProductDto
import com.example.shelfy.di.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProductRepository {

    suspend fun getProduct(barcode: String): OpenFoodProductDto? {
        return try {
            withContext(Dispatchers.IO) {
                val response = NetworkModule.api.getProductByBarcode(barcode)

                if (response.status == 1) {
                    response.product
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}