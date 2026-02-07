package com.stockmanagement.app.presentation.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.stockmanagement.app.presentation.ui.components.DashboardCard
import com.stockmanagement.app.presentation.ui.components.TransactionItem
import com.stockmanagement.app.presentation.ui.navigation.Screen
import com.stockmanagement.app.presentation.viewmodel.ProductViewModel
import com.stockmanagement.app.presentation.viewmodel.CategoryViewModel
import com.stockmanagement.app.presentation.viewmodel.SupplierViewModel
import com.stockmanagement.app.presentation.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    productViewModel: ProductViewModel? = null,
    categoryViewModel: CategoryViewModel? = null,
    supplierViewModel: SupplierViewModel? = null,
    transactionViewModel: TransactionViewModel? = null
) {
    val productCount by productViewModel?.productCount?.collectAsState() ?: remember { mutableStateOf(0) }
    val lowStockCount by productViewModel?.lowStockCount?.collectAsState() ?: remember { mutableStateOf(0) }
    val categories by categoryViewModel?.categories?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val suppliers by supplierViewModel?.suppliers?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val recentTransactions by transactionViewModel?.recentTransactions?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Inventory, "Products") },
                    label = { Text("Products") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Products.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Category, "Categories") },
                    label = { Text("Categories") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Categories.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Business, "Suppliers") },
                    label = { Text("Suppliers") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Suppliers.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assessment, "Reports") },
                    label = { Text("Reports") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Reports.route) }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Products",
                        value = productCount.toString(),
                        icon = Icons.Default.Inventory,
                        onClick = { navController.navigate(Screen.Products.route) }
                    )
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        title = "Low Stock",
                        value = lowStockCount.toString(),
                        icon = Icons.Default.Warning,
                        onClick = { navController.navigate(Screen.Inventory.route) }
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        title = "Categories",
                        value = categories.size.toString(),
                        icon = Icons.Default.Category,
                        onClick = { navController.navigate(Screen.Categories.route) }
                    )
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        title = "Suppliers",
                        value = suppliers.size.toString(),
                        icon = Icons.Default.Business,
                        onClick = { navController.navigate(Screen.Suppliers.route) }
                    )
                }
            }
            
            item {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            if (recentTransactions.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No recent transactions")
                        }
                    }
                }
            } else {
                items(recentTransactions) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}
