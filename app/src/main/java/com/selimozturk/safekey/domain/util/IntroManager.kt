package com.selimozturk.safekey.domain.util

import android.content.SharedPreferences
import javax.inject.Inject

class IntroManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun setIntro(isLogin: Boolean) {
        sharedPreferences.edit().putBoolean("Intro", isLogin).apply()
    }

    fun isIntroSkip(): Boolean {
        return sharedPreferences.getBoolean("Intro", false)
    }

}