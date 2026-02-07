package com.stockmanagement.app.data.database

import androidx.room.TypeConverter
import com.stockmanagement.app.data.entity.TransactionType

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}
