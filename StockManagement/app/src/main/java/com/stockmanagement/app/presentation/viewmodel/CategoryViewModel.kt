package com.stockmanagement.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockmanagement.app.data.entity.Category
import com.stockmanagement.app.data.repository.CategoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {
    val categories: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val rootCategories: StateFlow<List<Category>> = repository.getRootCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val searchResults: StateFlow<List<Category>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllCategories()
            } else {
                repository.searchCategories(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun insertCategory(category: Category, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.insertCategory(category)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to insert category")
            }
        }
    }
    
    fun updateCategory(category: Category, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateCategory(category)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update category")
            }
        }
    }
    
    fun deleteCategory(category: Category, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(category)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to delete category")
            }
        }
    }
    
    suspend fun getCategoryById(id: Long): Category? = repository.getCategoryById(id)
    
    fun getSubCategories(parentId: Long): Flow<List<Category>> = repository.getSubCategories(parentId)
}
