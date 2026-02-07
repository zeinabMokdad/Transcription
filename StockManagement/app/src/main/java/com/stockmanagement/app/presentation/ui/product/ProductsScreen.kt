package com.stockmanagement.app.presentation.ui.product

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
import com.stockmanagement.app.presentation.ui.components.ProductItem
import com.stockmanagement.app.presentation.ui.navigation.Screen
import com.stockmanagement.app.presentation.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductViewModel? = null
) {
    val products by viewModel?.searchResults?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val searchQuery by viewModel?.searchQuery?.collectAsState() ?: remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            if (showSearchBar) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel?.setSearchQuery(it) ?: Unit },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    onBackClick = { showSearchBar = false },
                    modifier = Modifier.fillMaxWidth()
                ) {}
            } else {
                TopAppBar(
                    title = { Text("Products") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(Icons.Default.Search, "Search")
                        }
                        IconButton(onClick = { /* Filter */ }) {
                            Icon(Icons.Default.FilterList, "Filter")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddProduct.route) }
            ) {
                Icon(Icons.Default.Add, "Add Product")
            }
        }
    ) { padding ->
        if (products.isEmpty()) {
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
                        imageVector = Icons.Default.Inventory,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("No products yet")
                    TextButton(onClick = { navController.navigate(Screen.AddProduct.route) }) {
                        Text("Add your first product")
                    }
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
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onClick = { navController.navigate(Screen.EditProduct.createRoute(product.id)) },
                        onDelete = { viewModel?.deleteProduct(product, {}, {}) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search products...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        },
        actions = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, "Clear")
                }
            }
        }
    )
}
