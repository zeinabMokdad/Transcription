package com.stockmanagement.app.presentation.ui.settings

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
fun SettingsScreen(
    navController: NavController
) {
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var lowStockAlerts by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Dark Theme", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "Use dark theme for the app",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = darkThemeEnabled,
                            onCheckedChange = { darkThemeEnabled = it }
                        )
                    }
                }
            }
            
            item {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Enable Notifications", style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    "Receive app notifications",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it }
                            )
                        }
                        
                        Divider()
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Low Stock Alerts", style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    "Get notified when stock is low",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = lowStockAlerts,
                                onCheckedChange = { lowStockAlerts = it },
                                enabled = notificationsEnabled
                            )
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        OutlinedButton(
                            onClick = { /* Backup data */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.Backup, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Backup Database")
                        }
                        
                        Divider()
                        
                        OutlinedButton(
                            onClick = { /* Restore data */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.RestorePage, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Restore Database")
                        }
                        
                        Divider()
                        
                        OutlinedButton(
                            onClick = { /* Export data */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export All Data")
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Stock Management App", style = MaterialTheme.typography.titleMedium)
                        Text("Version 1.0.0", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "A complete inventory management solution",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
