package com.selimozturk.safekey.domain.util

enum class PasswordCategory(val value: Int, val displayName: String) {
    CARD(0, "Card"),
    BROWSE(1, "Browser"),
    SOCIAL(2, "Social"),
    GAME(3, "Game"), ;
}