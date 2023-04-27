package com.selimozturk.safekey.data.source

import androidx.room.*
import com.selimozturk.safekey.domain.model.Password

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password")
    suspend fun getPasswords(): List<Password>

    @Query("SELECT * FROM password WHERE id = :id")
    suspend fun getPasswordById(id: Int): Password

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: Password)

    @Update
    suspend fun updatePassword(password: Password)

    @Query("DELETE FROM password WHERE id = :id")
    suspend fun deletePasswordById(id: Int)

}