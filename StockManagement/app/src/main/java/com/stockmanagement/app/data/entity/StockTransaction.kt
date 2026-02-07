package com.stockmanagement.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["productId"]), Index(value = ["timestamp"])]
)
data class StockTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val type: TransactionType,
    val quantity: Int,
    val balanceBefore: Int,
    val balanceAfter: Int,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    STOCK_IN,
    STOCK_OUT,
    ADJUSTMENT
}
