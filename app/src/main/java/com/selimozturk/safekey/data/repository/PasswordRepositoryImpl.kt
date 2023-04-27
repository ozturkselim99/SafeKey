package com.selimozturk.safekey.data.repository

import com.selimozturk.safekey.data.source.PasswordDao
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.repository.PasswordRepository

class PasswordRepositoryImpl(
    private val dao: PasswordDao
) : PasswordRepository {

    override suspend fun getPasswords(): List<Password> {
        return dao.getPasswords()
    }

    override suspend fun getPasswordById(id: Int): Password {
        return dao.getPasswordById(id)
    }

    override suspend fun insertPassword(password: Password) {
        return dao.insertPassword(password)
    }

    override suspend fun updatePassword(password: Password) {
        return dao.updatePassword(password)
    }

    override suspend fun deletePasswordById(id: Int) {
        return dao.deletePasswordById(id)
    }

}