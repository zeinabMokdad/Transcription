package com.stockmanagement.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.stockmanagement.app.data.database.StockDatabase
import com.stockmanagement.app.data.repository.*
import com.stockmanagement.app.presentation.ui.navigation.AppNavigation
import com.stockmanagement.app.presentation.ui.theme.StockManagementTheme
import com.stockmanagement.app.presentation.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database
        val database = StockDatabase.getDatabase(applicationContext)
        
        // Initialize repositories
        val productRepository = ProductRepository(
            database.productDao(),
            database.stockTransactionDao()
        )
        val categoryRepository = CategoryRepository(database.categoryDao())
        val supplierRepository = SupplierRepository(database.supplierDao())
        val transactionRepository = TransactionRepository(database.stockTransactionDao())
        
        // Initialize ViewModels
        val productViewModel = ProductViewModel(productRepository)
        val categoryViewModel = CategoryViewModel(categoryRepository)
        val supplierViewModel = SupplierViewModel(supplierRepository)
        val transactionViewModel = TransactionViewModel(transactionRepository)
        
        setContent {
            StockManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    // Store ViewModels in CompositionLocal or pass them through navigation
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
