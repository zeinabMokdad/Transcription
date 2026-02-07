package com.stockmanagement.app.presentation.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.stockmanagement.app.data.entity.TransactionType
import com.stockmanagement.app.presentation.ui.components.ProductItem
import com.stockmanagement.app.presentation.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: ProductViewModel? = null
) {
    val lowStockProducts by viewModel?.lowStockProducts?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    var showStockDialog by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf<Long?>(null) }
    var stockQuantity by remember { mutableStateOf("") }
    var stockNote by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.STOCK_IN) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Low Stock Items") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (lowStockProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("All products have sufficient stock!")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                "${lowStockProducts.size} products are low on stock",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
                
                items(lowStockProducts) { product ->
                    ProductItem(
                        product = product,
                        onClick = {
                            selectedProductId = product.id
                            showStockDialog = true
                        },
                        onDelete = {}
                    )
                }
            }
        }
        
        if (showStockDialog && selectedProductId != null) {
            AlertDialog(
                onDismissRequest = { showStockDialog = false },
                title = { Text("Adjust Stock") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = transactionType == TransactionType.STOCK_IN,
                                onClick = { transactionType = TransactionType.STOCK_IN },
                                label = { Text("Stock In") }
                            )
                            FilterChip(
                                selected = transactionType == TransactionType.STOCK_OUT,
                                onClick = { transactionType = TransactionType.STOCK_OUT },
                                label = { Text("Stock Out") }
                            )
                            FilterChip(
                                selected = transactionType == TransactionType.ADJUSTMENT,
                                onClick = { transactionType = TransactionType.ADJUSTMENT },
                                label = { Text("Adjust") }
                            )
                        }
                        
                        OutlinedTextField(
                            value = stockQuantity,
                            onValueChange = { stockQuantity = it },
                            label = { Text("Quantity") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = stockNote,
                            onValueChange = { stockNote = it },
                            label = { Text("Note (Optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val qty = stockQuantity.toIntOrNull()
                            if (qty != null && qty > 0) {
                                viewModel?.adjustStock(
                                    selectedProductId!!,
                                    qty,
                                    transactionType,
                                    stockNote.takeIf { it.isNotBlank() },
                                    onSuccess = {
                                        showStockDialog = false
                                        stockQuantity = ""
                                        stockNote = ""
                                    },
                                    onError = { }
                                )
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStockDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
