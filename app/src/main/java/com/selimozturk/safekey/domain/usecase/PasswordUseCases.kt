package com.selimozturk.safekey.domain.usecase

data class PasswordUseCases(
    val getPasswords: GetPasswords,
    val deletePasswordById: DeletePasswordById,
    val insertPassword: InsertPassword,
    val getPasswordById: GetPasswordById,
    val updatePassword: UpdatePassword,
)