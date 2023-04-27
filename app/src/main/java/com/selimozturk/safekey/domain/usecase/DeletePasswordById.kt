package com.selimozturk.safekey.domain.usecase

import com.selimozturk.safekey.domain.repository.PasswordRepository

class DeletePasswordById(private val repository: PasswordRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deletePasswordById(id)
    }
}