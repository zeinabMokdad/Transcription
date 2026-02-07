package com.stockmanagement.app.data.repository

import com.stockmanagement.app.data.dao.CategoryDao
import com.stockmanagement.app.data.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)
    
    fun getRootCategories(): Flow<List<Category>> = categoryDao.getRootCategories()
    
    fun getSubCategories(parentId: Long): Flow<List<Category>> = categoryDao.getSubCategories(parentId)
    
    fun searchCategories(query: String): Flow<List<Category>> = categoryDao.searchCategories(query)
    
    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
}
