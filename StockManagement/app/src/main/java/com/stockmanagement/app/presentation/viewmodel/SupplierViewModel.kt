package com.stockmanagement.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockmanagement.app.data.entity.Supplier
import com.stockmanagement.app.data.repository.SupplierRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SupplierViewModel(private val repository: SupplierRepository) : ViewModel() {
    val suppliers: StateFlow<List<Supplier>> = repository.getAllSuppliers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val searchResults: StateFlow<List<Supplier>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllSuppliers()
            } else {
                repository.searchSuppliers(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun insertSupplier(supplier: Supplier, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.insertSupplier(supplier)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to insert supplier")
            }
        }
    }
    
    fun updateSupplier(supplier: Supplier, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateSupplier(supplier)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update supplier")
            }
        }
    }
    
    fun deleteSupplier(supplier: Supplier, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteSupplier(supplier)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to delete supplier")
            }
        }
    }
    
    suspend fun getSupplierById(id: Long): Supplier? = repository.getSupplierById(id)
}
