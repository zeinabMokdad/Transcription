package com.stockmanagement.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockmanagement.app.data.entity.Product
import com.stockmanagement.app.data.entity.TransactionType
import com.stockmanagement.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val products: StateFlow<List<Product>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val lowStockProducts: StateFlow<List<Product>> = repository.getLowStockProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val productCount: StateFlow<Int> = repository.getProductCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val lowStockCount: StateFlow<Int> = repository.getLowStockCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val searchResults: StateFlow<List<Product>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllProducts()
            } else {
                repository.searchProducts(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun insertProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.insertProduct(product)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to insert product")
            }
        }
    }
    
    fun updateProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update product")
            }
        }
    }
    
    fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(product)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to delete product")
            }
        }
    }
    
    suspend fun getProductById(id: Long): Product? = repository.getProductById(id)
    
    suspend fun getProductByBarcode(barcode: String): Product? = repository.getProductByBarcode(barcode)
    
    fun adjustStock(productId: Long, quantity: Int, type: TransactionType, note: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val success = repository.adjustStock(productId, quantity, type, note)
                if (success) {
                    onSuccess()
                } else {
                    onError("Failed to adjust stock")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Failed to adjust stock")
            }
        }
    }
}
