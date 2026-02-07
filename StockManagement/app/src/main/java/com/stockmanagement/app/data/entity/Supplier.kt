package com.stockmanagement.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "suppliers",
    indices = [Index(value = ["name"], unique = true)]
)
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val contactPerson: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
