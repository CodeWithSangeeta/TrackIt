package com.practice.trackit.ui.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.trackit.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.login(email, password)
            _loading.value = false

            result.onSuccess {
                _error.value = null
                onSuccess()
            }.onFailure {
                _error.value = it.message
            }
        }
    }

    fun signup(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.signup(email, password)
            _loading.value = false

            result.onSuccess {
                _error.value = null
                onSuccess()
            }.onFailure {
                _error.value = it.message
            }
        }
    }


}
