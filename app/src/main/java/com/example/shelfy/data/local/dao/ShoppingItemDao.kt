package com.example.shelfy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shelfy.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItemEntity): Long

    @Delete
    suspend fun delete(item: ShoppingItemEntity)

    @Query("UPDATE shopping_items SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompleted(id: Long, completed: Boolean)

    @Query("SELECT * FROM shopping_items ORDER BY id ASC")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>

    @Query("DELETE FROM shopping_items WHERE isCompleted = 1")
    suspend fun clearCompleted()
}
