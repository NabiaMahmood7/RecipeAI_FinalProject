package week11.st482988.recipeai_finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st482988.recipeai_finalproject.data.model.User
import week11.st482988.recipeai_finalproject.data.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        _currentUser.value = authRepository.getCurrentUser()
    }

    fun register(email: String, password: String, fullName: String) {
        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            _uiState.value = AuthUiState.Error("All fields are required")
            return
        }

        if (password.length < 8) {
            _uiState.value = AuthUiState.Error("Password must be at least 8 characters")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = AuthUiState.Loading

            val result = authRepository.register(email, password, fullName)

            result.onSuccess { user ->
                _currentUser.value = user
                _uiState.value = AuthUiState.Success("Registration successful")
                _isLoading.value = false
            }

            result.onFailure { error ->
                _uiState.value = AuthUiState.Error(
                    error.message ?: "Registration failed. Please try again."
                )
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _uiState.value = AuthUiState.Error("Email and password are required")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = AuthUiState.Loading

            val result = authRepository.login(email, password)

            result.onSuccess { user ->
                _currentUser.value = user
                _uiState.value = AuthUiState.Success("Login successful")
                _isLoading.value = false
            }

            result.onFailure { error ->
                _uiState.value = AuthUiState.Error(
                    error.message ?: "Login failed. Please try again."
                )
                _isLoading.value = false
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = AuthUiState.Loading

            val result = authRepository.signInWithGoogle(idToken)

            result.onSuccess { user ->
                _currentUser.value = user
                _uiState.value = AuthUiState.Success("Login successful")
                _isLoading.value = false
            }

            result.onFailure { error ->
                _uiState.value = AuthUiState.Error(
                    error.message ?: "Google sign-in failed. Please try again."
                )
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().onSuccess {
                _currentUser.value = null
                _uiState.value = AuthUiState.Idle
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _uiState.value = AuthUiState.Error("Email is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            authRepository.resetPassword(email).onSuccess {
                _uiState.value = AuthUiState.Success("Password reset email sent")
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(
                    error.message ?: "Failed to send reset email"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val message: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}