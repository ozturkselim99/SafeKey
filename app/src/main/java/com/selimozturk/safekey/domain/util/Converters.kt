package com.selimozturk.safekey.domain.util

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromPasswordCategory(value: PasswordCategory): Int {
        return value.value
    }

    @TypeConverter
    fun toPasswordCategory(value: Int): PasswordCategory {
        return enumValues<PasswordCategory>().first { it.value == value }
    }
}