package com.selimozturk.safekey.domain.repository

import com.selimozturk.safekey.domain.model.Password

interface PasswordRepository {

    suspend fun getPasswords(): List<Password>

    suspend fun getPasswordById(id: Int): Password

    suspend fun insertPassword(password: Password)

    suspend fun updatePassword(password: Password)

    suspend fun deletePasswordById(id: Int)

}