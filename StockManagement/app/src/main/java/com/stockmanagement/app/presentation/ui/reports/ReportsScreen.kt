package com.stockmanagement.app.presentation.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Generate Reports",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            
            item {
                ReportCard(
                    title = "Stock Summary Report",
                    description = "Complete inventory overview with current stock levels",
                    icon = Icons.Default.Inventory2,
                    onGeneratePDF = { /* Generate PDF */ },
                    onGenerateCSV = { /* Generate CSV */ }
                )
            }
            
            item {
                ReportCard(
                    title = "Low Stock Report",
                    description = "Products below minimum stock threshold",
                    icon = Icons.Default.Warning,
                    onGeneratePDF = { /* Generate PDF */ },
                    onGenerateCSV = { /* Generate CSV */ }
                )
            }
            
            item {
                ReportCard(
                    title = "Transaction History",
                    description = "Complete history of all stock movements",
                    icon = Icons.Default.History,
                    onGeneratePDF = { /* Generate PDF */ },
                    onGenerateCSV = { /* Generate CSV */ }
                )
            }
            
            item {
                ReportCard(
                    title = "Category Analysis",
                    description = "Stock distribution across categories",
                    icon = Icons.Default.PieChart,
                    onGeneratePDF = { /* Generate PDF */ },
                    onGenerateCSV = { /* Generate CSV */ }
                )
            }
            
            item {
                ReportCard(
                    title = "Supplier Report",
                    description = "Products by supplier with stock information",
                    icon = Icons.Default.Business,
                    onGeneratePDF = { /* Generate PDF */ },
                    onGenerateCSV = { /* Generate CSV */ }
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onGeneratePDF: () -> Unit,
    onGenerateCSV: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onGeneratePDF,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PDF")
                }
                OutlinedButton(
                    onClick = onGenerateCSV,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CSV")
                }
            }
        }
    }
}
