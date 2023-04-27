package com.selimozturk.safekey.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.util.Converters

@Database(
    entities = [Password::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PasswordDatabase : RoomDatabase() {

    abstract val passwordDao: PasswordDao

    companion object {
        const val DATABASE_NAME = "passwords_db"
    }

}