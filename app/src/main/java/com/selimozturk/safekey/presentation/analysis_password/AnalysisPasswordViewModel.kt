package com.selimozturk.safekey.presentation.analysis_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.usecase.PasswordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisPasswordViewModel @Inject constructor(
    private val passwordUseCases: PasswordUseCases,
) : ViewModel() {

    private val _passwords = MutableLiveData<List<Password>>()
    val passwords: LiveData<List<Password>> get() = _passwords

    fun getPasswords() = viewModelScope.launch {
        _passwords.value = passwordUseCases.getPasswords()
    }

}