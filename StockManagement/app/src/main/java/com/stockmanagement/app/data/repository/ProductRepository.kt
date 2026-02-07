package com.stockmanagement.app.data.repository

import com.stockmanagement.app.data.dao.ProductDao
import com.stockmanagement.app.data.dao.StockTransactionDao
import com.stockmanagement.app.data.entity.Product
import com.stockmanagement.app.data.entity.StockTransaction
import com.stockmanagement.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val transactionDao: StockTransactionDao
) {
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    
    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)
    
    suspend fun getProductBySku(sku: String): Product? = productDao.getProductBySku(sku)
    
    suspend fun getProductByBarcode(barcode: String): Product? = productDao.getProductByBarcode(barcode)
    
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>> = productDao.getProductsByCategory(categoryId)
    
    fun getProductsBySupplier(supplierId: Long): Flow<List<Product>> = productDao.getProductsBySupplier(supplierId)
    
    fun getLowStockProducts(): Flow<List<Product>> = productDao.getLowStockProducts()
    
    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)
    
    fun getProductCount(): Flow<Int> = productDao.getProductCount()
    
    fun getLowStockCount(): Flow<Int> = productDao.getLowStockCount()
    
    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)
    
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
    
    suspend fun adjustStock(productId: Long, quantity: Int, type: TransactionType, note: String? = null): Boolean {
        return try {
            val product = productDao.getProductById(productId) ?: return false
            val newQuantity = when (type) {
                TransactionType.STOCK_IN -> product.quantity + quantity
                TransactionType.STOCK_OUT -> (product.quantity - quantity).coerceAtLeast(0)
                TransactionType.ADJUSTMENT -> quantity
            }
            
            val transaction = StockTransaction(
                productId = productId,
                type = type,
                quantity = quantity,
                balanceBefore = product.quantity,
                balanceAfter = newQuantity,
                note = note,
                timestamp = System.currentTimeMillis()
            )
            
            transactionDao.insertTransaction(transaction)
            productDao.updateProductQuantity(productId, newQuantity, System.currentTimeMillis())
            true
        } catch (e: Exception) {
            false
        }
    }
}
