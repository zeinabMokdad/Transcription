package com.stockmanagement.app.data.dao

import androidx.room.*
import com.stockmanagement.app.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?
    
    @Query("SELECT * FROM categories WHERE parentCategoryId IS NULL ORDER BY name ASC")
    fun getRootCategories(): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE parentCategoryId = :parentId ORDER BY name ASC")
    fun getSubCategories(parentId: Long): Flow<List<Category>>
    
    @Query("SELECT * FROM categories WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchCategories(query: String): Flow<List<Category>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long
    
    @Update
    suspend fun updateCategory(category: Category)
    
    @Delete
    suspend fun deleteCategory(category: Category)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)
}
