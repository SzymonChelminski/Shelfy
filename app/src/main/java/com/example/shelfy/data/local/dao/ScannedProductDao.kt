package com.example.shelfy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shelfy.data.local.entity.ScannedProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ScannedProductEntity)

    @Delete
    suspend fun delete(product: ScannedProductEntity)

    @Update
    suspend fun update(product: ScannedProductEntity)

    @Query("SELECT * FROM scanned_products ORDER BY expiryDateMillis ASC")
    fun getAllProducts(): Flow<List<ScannedProductEntity>>

    @Query("SELECT * FROM scanned_products WHERE barcode = :barcode")
    fun getByBarcode(barcode: String): Flow<ScannedProductEntity?>

    @Query("SELECT * FROM scanned_products WHERE expiryDateMillis <= :thresholdMillis")
    suspend fun getProductsExpiringBefore(thresholdMillis: Long): List<ScannedProductEntity>

    @Query("DELETE FROM scanned_products")
    suspend fun deleteAll()
}
