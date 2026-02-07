package com.stockmanagement.app.data.repository

import com.stockmanagement.app.data.dao.StockTransactionDao
import com.stockmanagement.app.data.entity.StockTransaction
import com.stockmanagement.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: StockTransactionDao) {
    fun getAllTransactions(): Flow<List<StockTransaction>> = transactionDao.getAllTransactions()
    
    suspend fun getTransactionById(id: Long): StockTransaction? = transactionDao.getTransactionById(id)
    
    fun getTransactionsByProduct(productId: Long): Flow<List<StockTransaction>> = transactionDao.getTransactionsByProduct(productId)
    
    fun getTransactionsByType(type: TransactionType): Flow<List<StockTransaction>> = transactionDao.getTransactionsByType(type)
    
    fun getTransactionsByDateRange(startTime: Long, endTime: Long): Flow<List<StockTransaction>> = transactionDao.getTransactionsByDateRange(startTime, endTime)
    
    fun getRecentTransactions(limit: Int): Flow<List<StockTransaction>> = transactionDao.getRecentTransactions(limit)
}
