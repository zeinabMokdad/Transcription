package com.stockmanagement.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stockmanagement.app.data.dao.*
import com.stockmanagement.app.data.entity.*

@Database(
    entities = [
        Product::class,
        Category::class,
        Supplier::class,
        StockTransaction::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class StockDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun supplierDao(): SupplierDao
    abstract fun stockTransactionDao(): StockTransactionDao
    
    companion object {
        @Volatile
        private var INSTANCE: StockDatabase? = null
        
        fun getDatabase(context: Context): StockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StockDatabase::class.java,
                    "stock_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
