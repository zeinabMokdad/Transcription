package com.stockmanagement.app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stockmanagement.app.presentation.ui.dashboard.DashboardScreen
import com.stockmanagement.app.presentation.ui.product.ProductsScreen
import com.stockmanagement.app.presentation.ui.product.AddEditProductScreen
import com.stockmanagement.app.presentation.ui.inventory.InventoryScreen
import com.stockmanagement.app.presentation.ui.category.CategoriesScreen
import com.stockmanagement.app.presentation.ui.supplier.SuppliersScreen
import com.stockmanagement.app.presentation.ui.reports.ReportsScreen
import com.stockmanagement.app.presentation.ui.settings.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        
        composable(Screen.Products.route) {
            ProductsScreen(navController)
        }
        
        composable(Screen.AddProduct.route) {
            AddEditProductScreen(navController, productId = null)
        }
        
        composable(
            route = Screen.EditProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId")
            AddEditProductScreen(navController, productId)
        }
        
        composable(Screen.Inventory.route) {
            InventoryScreen(navController)
        }
        
        composable(Screen.Categories.route) {
            CategoriesScreen(navController)
        }
        
        composable(Screen.Suppliers.route) {
            SuppliersScreen(navController)
        }
        
        composable(Screen.Reports.route) {
            ReportsScreen(navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
