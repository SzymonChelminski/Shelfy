package com.example.shelfy.di

import android.content.Context
import com.example.shelfy.data.local.ShelfyDatabase
import com.example.shelfy.data.local.dao.ScannedProductDao
import com.example.shelfy.data.repository.ScannedProductRepository

object DatabaseModule {

    lateinit var database: ShelfyDatabase
        private set

    lateinit var dao: ScannedProductDao
        private set

    lateinit var repository: ScannedProductRepository
        private set

    fun init(context: Context) {
        database = ShelfyDatabase.getInstance(context)
        dao = database.scannedProductDao()
        repository = ScannedProductRepository(dao)
    }
}
