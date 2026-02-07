package com.stockmanagement.app.presentation.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.stockmanagement.app.data.entity.Product
import com.stockmanagement.app.presentation.viewmodel.ProductViewModel
import com.stockmanagement.app.presentation.viewmodel.CategoryViewModel
import com.stockmanagement.app.presentation.viewmodel.SupplierViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    navController: NavController,
    productId: Long?,
    productViewModel: ProductViewModel? = null,
    categoryViewModel: CategoryViewModel? = null,
    supplierViewModel: SupplierViewModel? = null
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var lowStockThreshold by remember { mutableStateOf("10") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val categories by categoryViewModel?.categories?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val suppliers by supplierViewModel?.suppliers?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedSupplierId by remember { mutableStateOf<Long?>(null) }
    
    LaunchedEffect(productId) {
        productId?.let {
            productViewModel?.getProductById(it)?.let { product ->
                name = product.name
                sku = product.sku
                barcode = product.barcode ?: ""
                description = product.description ?: ""
                price = product.price.toString()
                quantity = product.quantity.toString()
                lowStockThreshold = product.lowStockThreshold.toString()
                selectedCategoryId = product.categoryId
                selectedSupplierId = product.supplierId
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) "Add Product" else "Edit Product") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Barcode") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { /* Scan barcode */ }) {
                        Icon(Icons.Default.QrCodeScanner, "Scan")
                    }
                }
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price *") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity *") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
            
            OutlinedTextField(
                value = lowStockThreshold,
                onValueChange = { lowStockThreshold = it },
                label = { Text("Low Stock Threshold") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Button(
                onClick = {
                    if (name.isBlank() || sku.isBlank() || price.isBlank() || quantity.isBlank()) {
                        errorMessage = "Please fill all required fields"
                        return@Button
                    }
                    
                    val product = Product(
                        id = productId ?: 0,
                        name = name,
                        sku = sku,
                        barcode = barcode.takeIf { it.isNotBlank() },
                        categoryId = selectedCategoryId,
                        supplierId = selectedSupplierId,
                        description = description.takeIf { it.isNotBlank() },
                        price = price.toDoubleOrNull() ?: 0.0,
                        quantity = quantity.toIntOrNull() ?: 0,
                        lowStockThreshold = lowStockThreshold.toIntOrNull() ?: 10
                    )
                    
                    scope.launch {
                        if (productId == null) {
                            productViewModel?.insertProduct(
                                product,
                                onSuccess = { navController.navigateUp() },
                                onError = { errorMessage = it }
                            )
                        } else {
                            productViewModel?.updateProduct(
                                product,
                                onSuccess = { navController.navigateUp() },
                                onError = { errorMessage = it }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productId == null) "Add Product" else "Update Product")
            }
        }
    }
}
