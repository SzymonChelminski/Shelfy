package com.example.shelfy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shelfy.data.local.dao.ScannedProductDao
import com.example.shelfy.data.local.dao.ShoppingItemDao
import com.example.shelfy.data.local.entity.ScannedProductEntity
import com.example.shelfy.data.local.entity.ShoppingItemEntity

@Database(entities = [ScannedProductEntity::class, ShoppingItemEntity::class], version = 5, exportSchema = false)
abstract class ShelfyDatabase : RoomDatabase() {

    abstract fun scannedProductDao(): ScannedProductDao

    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        @Volatile
        private var instance: ShelfyDatabase? = null

        fun getInstance(context: Context): ShelfyDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ShelfyDatabase::class.java,
                    "shelfy.db"
                ).fallbackToDestructiveMigration(true).build().also { instance = it }
            }
    }
}
