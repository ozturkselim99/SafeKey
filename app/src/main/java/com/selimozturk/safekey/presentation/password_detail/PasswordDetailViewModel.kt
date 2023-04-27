package com.selimozturk.safekey.presentation.password_detail

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
class PasswordDetailViewModel @Inject constructor(
    private val passwordUseCases: PasswordUseCases,
) : ViewModel() {

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password> get() = _password


    fun getPasswordById(id: Int) = viewModelScope.launch {
        _password.value = passwordUseCases.getPasswordById(id)
    }

    fun deletePasswordById(id: Int) = viewModelScope.launch {
        passwordUseCases.deletePasswordById(id)
    }

}