package com.stockmanagement.app.data.dao

import androidx.room.*
import com.stockmanagement.app.data.entity.StockTransaction
import com.stockmanagement.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface StockTransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<StockTransaction>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): StockTransaction?
    
    @Query("SELECT * FROM transactions WHERE productId = :productId ORDER BY timestamp DESC")
    fun getTransactionsByProduct(productId: Long): Flow<List<StockTransaction>>
    
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<StockTransaction>>
    
    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getTransactionsByDateRange(startTime: Long, endTime: Long): Flow<List<StockTransaction>>
    
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<StockTransaction>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: StockTransaction): Long
    
    @Delete
    suspend fun deleteTransaction(transaction: StockTransaction)
    
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}
