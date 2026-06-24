package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = AuthRepository.login(email, password)
            if (res.isSuccess) {
                _uiState.value = AuthUiState.Success
            } else {
                _uiState.value = AuthUiState.Error(res.exceptionOrNull()?.message ?: "Login failed. Please verify credentials.")
            }
        }
    }

    fun register(email: String, phone: String, password: String, referralCode: String?) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = AuthRepository.register(email, phone, password, referralCode)
            if (res.isSuccess) {
                _uiState.value = AuthUiState.Success
            } else {
                _uiState.value = AuthUiState.Error(res.exceptionOrNull()?.message ?: "Registration failed.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            AuthRepository.logout()
            _uiState.value = AuthUiState.Idle
        }
    }

    fun checkSession(): Boolean {
        return AuthRepository.isLoggedIn()
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
