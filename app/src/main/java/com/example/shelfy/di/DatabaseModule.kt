package com.example.shelfy.di

import android.content.Context
import com.example.shelfy.data.local.ShelfyDatabase
import com.example.shelfy.data.local.dao.ScannedProductDao
import com.example.shelfy.data.local.dao.ShoppingItemDao
import com.example.shelfy.data.repository.ScannedProductRepository
import com.example.shelfy.data.repository.ShoppingRepository

object DatabaseModule {

    lateinit var database: ShelfyDatabase
        private set

    lateinit var dao: ScannedProductDao
        private set

    lateinit var repository: ScannedProductRepository
        private set

    lateinit var shoppingDao: ShoppingItemDao
        private set

    lateinit var shoppingRepository: ShoppingRepository
        private set

    fun init(context: Context) {
        database = ShelfyDatabase.getInstance(context)
        dao = database.scannedProductDao()
        repository = ScannedProductRepository(dao)
        shoppingDao = database.shoppingItemDao()
        shoppingRepository = ShoppingRepository(shoppingDao)
    }
}
