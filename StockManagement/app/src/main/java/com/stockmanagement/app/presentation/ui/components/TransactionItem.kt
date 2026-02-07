package com.stockmanagement.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stockmanagement.app.data.entity.StockTransaction
import com.stockmanagement.app.data.entity.TransactionType
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionItem(
    transaction: StockTransaction,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val icon = when (transaction.type) {
        TransactionType.STOCK_IN -> Icons.Default.Add
        TransactionType.STOCK_OUT -> Icons.Default.Remove
        TransactionType.ADJUSTMENT -> Icons.Default.Edit
    }
    val iconColor = when (transaction.type) {
        TransactionType.STOCK_IN -> MaterialTheme.colorScheme.primary
        TransactionType.STOCK_OUT -> MaterialTheme.colorScheme.error
        TransactionType.ADJUSTMENT -> MaterialTheme.colorScheme.tertiary
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = iconColor.copy(alpha = 0.1f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = transaction.type.name,
                        modifier = Modifier.padding(8.dp),
                        tint = iconColor
                    )
                }
                Column {
                    Text(
                        text = transaction.type.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = dateFormat.format(Date(transaction.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    transaction.note?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.type == TransactionType.STOCK_OUT) "-" else "+"}${transaction.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
                Text(
                    text = "Balance: ${transaction.balanceAfter}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
