package com.example.shelfy.data.repository

import com.example.shelfy.data.local.dao.ScannedProductDao
import com.example.shelfy.data.local.entity.ScannedProductEntity
import kotlinx.coroutines.flow.Flow

class ScannedProductRepository(private val dao: ScannedProductDao) {

    fun getAllProducts(): Flow<List<ScannedProductEntity>> = dao.getAllProducts()

    fun getByBarcode(barcode: String): Flow<ScannedProductEntity?> = dao.getByBarcode(barcode)

    suspend fun insert(product: ScannedProductEntity) = dao.insert(product)

    suspend fun delete(product: ScannedProductEntity) = dao.delete(product)
}
