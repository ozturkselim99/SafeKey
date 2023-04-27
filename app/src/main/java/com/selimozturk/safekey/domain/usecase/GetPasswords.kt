package com.selimozturk.safekey.domain.usecase

import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.repository.PasswordRepository

class GetPasswords(private val repository: PasswordRepository) {
    suspend operator fun invoke(): List<Password> {
        return repository.getPasswords()
    }
}