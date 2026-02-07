package com.stockmanagement.app.data.repository

import com.stockmanagement.app.data.dao.SupplierDao
import com.stockmanagement.app.data.entity.Supplier
import kotlinx.coroutines.flow.Flow

class SupplierRepository(private val supplierDao: SupplierDao) {
    fun getAllSuppliers(): Flow<List<Supplier>> = supplierDao.getAllSuppliers()
    
    suspend fun getSupplierById(id: Long): Supplier? = supplierDao.getSupplierById(id)
    
    fun searchSuppliers(query: String): Flow<List<Supplier>> = supplierDao.searchSuppliers(query)
    
    suspend fun insertSupplier(supplier: Supplier): Long = supplierDao.insertSupplier(supplier)
    
    suspend fun updateSupplier(supplier: Supplier) = supplierDao.updateSupplier(supplier)
    
    suspend fun deleteSupplier(supplier: Supplier) = supplierDao.deleteSupplier(supplier)
}
