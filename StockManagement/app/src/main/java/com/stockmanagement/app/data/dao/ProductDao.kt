package com.stockmanagement.app.data.dao

import androidx.room.*
import com.stockmanagement.app.data.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?
    
    @Query("SELECT * FROM products WHERE sku = :sku")
    suspend fun getProductBySku(sku: String): Product?
    
    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): Product?
    
    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE supplierId = :supplierId ORDER BY name ASC")
    fun getProductsBySupplier(supplierId: Long): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE quantity <= lowStockThreshold ORDER BY quantity ASC")
    fun getLowStockProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchProducts(query: String): Flow<List<Product>>
    
    @Query("SELECT COUNT(*) FROM products")
    fun getProductCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM products WHERE quantity <= lowStockThreshold")
    fun getLowStockCount(): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)
    
    @Query("UPDATE products SET quantity = :quantity, updatedAt = :timestamp WHERE id = :productId")
    suspend fun updateProductQuantity(productId: Long, quantity: Int, timestamp: Long)
}
