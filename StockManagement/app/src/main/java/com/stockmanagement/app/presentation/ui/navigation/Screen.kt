package com.stockmanagement.app.presentation.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Products : Screen("products")
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: Long) = "product/$productId"
    }
    object AddProduct : Screen("add_product")
    object EditProduct : Screen("edit_product/{productId}") {
        fun createRoute(productId: Long) = "edit_product/$productId"
    }
    object Inventory : Screen("inventory")
    object Categories : Screen("categories")
    object Suppliers : Screen("suppliers")
    object Reports : Screen("reports")
    object Settings : Screen("settings")
    object BarcodeScanner : Screen("barcode_scanner")
}
