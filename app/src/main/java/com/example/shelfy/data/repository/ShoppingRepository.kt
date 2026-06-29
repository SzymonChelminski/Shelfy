package com.example.shelfy.data.repository

import com.example.shelfy.data.local.dao.ShoppingItemDao
import com.example.shelfy.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val dao: ShoppingItemDao) {

    fun getAllItems(): Flow<List<ShoppingItemEntity>> = dao.getAllItems()

    suspend fun insert(item: ShoppingItemEntity) = dao.insert(item)

    suspend fun delete(item: ShoppingItemEntity) = dao.delete(item)

    suspend fun updateCompleted(id: Long, completed: Boolean) = dao.updateCompleted(id, completed)

    suspend fun clearCompleted() = dao.clearCompleted()
}
