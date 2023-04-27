package com.selimozturk.safekey.domain.usecase

import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.repository.PasswordRepository

class UpdatePassword(private val repository: PasswordRepository) {
    suspend operator fun invoke(password: Password) {
        repository.updatePassword(password)
    }
}