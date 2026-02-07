package com.stockmanagement.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockmanagement.app.data.entity.StockTransaction
import com.stockmanagement.app.data.entity.TransactionType
import com.stockmanagement.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {
    val allTransactions: StateFlow<List<StockTransaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val recentTransactions: StateFlow<List<StockTransaction>> = repository.getRecentTransactions(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun getTransactionsByProduct(productId: Long): Flow<List<StockTransaction>> = 
        repository.getTransactionsByProduct(productId)
    
    fun getTransactionsByType(type: TransactionType): Flow<List<StockTransaction>> = 
        repository.getTransactionsByType(type)
    
    fun getTransactionsByDateRange(startTime: Long, endTime: Long): Flow<List<StockTransaction>> = 
        repository.getTransactionsByDateRange(startTime, endTime)
}
