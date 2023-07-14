package com.selimozturk.safekey.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.selimozturk.safekey.domain.util.PasswordCategory

@Entity
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var userId: String,
    var name: String,
    var password: String,
    var lastEditedDate: Long,
    var createdDate: Long,
    var category: PasswordCategory,
    var isAutoFillChecked: Boolean = false,
)
