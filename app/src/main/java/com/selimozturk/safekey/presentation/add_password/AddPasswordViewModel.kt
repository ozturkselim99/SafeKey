package com.selimozturk.safekey.presentation.add_password

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
class AddPasswordViewModel @Inject constructor(
    private val passwordUseCases: PasswordUseCases,
) : ViewModel() {

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password> get() = _password


    fun getPasswordById(id: Int) = viewModelScope.launch {
        _password.value = passwordUseCases.getPasswordById(id)
    }

    fun insertPassword(password: Password) = viewModelScope.launch {
        passwordUseCases.insertPassword(password)
    }

    fun updatePassword(password: Password) = viewModelScope.launch {
        passwordUseCases.updatePassword(password)
    }

}