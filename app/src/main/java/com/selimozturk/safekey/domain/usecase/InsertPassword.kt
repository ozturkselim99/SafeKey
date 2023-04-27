package com.selimozturk.safekey.domain.usecase

import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.repository.PasswordRepository

class InsertPassword(private val repository: PasswordRepository) {
    suspend operator fun invoke(password: Password) {
        repository.insertPassword(password)
    }
}