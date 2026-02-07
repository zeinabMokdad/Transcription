package com.stockmanagement.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Supplier::class,
            parentColumns = ["id"],
            childColumns = ["supplierId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["sku"], unique = true),
        Index(value = ["barcode"]),
        Index(value = ["categoryId"]),
        Index(value = ["supplierId"])
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val sku: String,
    val barcode: String? = null,
    val categoryId: Long? = null,
    val supplierId: Long? = null,
    val description: String? = null,
    val price: Double = 0.0,
    val quantity: Int = 0,
    val lowStockThreshold: Int = 10,
    val imagePath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
