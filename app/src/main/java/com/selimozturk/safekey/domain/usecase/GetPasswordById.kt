package com.selimozturk.safekey.domain.usecase

import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.repository.PasswordRepository

class GetPasswordById(private val repository: PasswordRepository) {
    suspend operator fun invoke(id: Int): Password {
        return repository.getPasswordById(id)
    }
}