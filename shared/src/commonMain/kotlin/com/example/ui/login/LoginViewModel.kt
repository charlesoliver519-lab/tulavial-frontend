package com.example.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.data.model.UsuarioDto
import com.example.data.repository.AuthRepository

data class LoginUiState(
    val usuario: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onUsuarioChange(valor: String) = _state.update { it.copy(usuario = valor, error = null) }
    fun onPasswordChange(valor: String) = _state.update { it.copy(password = valor, error = null) }

    fun login(onExito: (UsuarioDto) -> Unit) {
        val s = _state.value
        if (s.usuario.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Ingresa usuario y contraseña") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }
            repository.login(s.usuario.trim(), s.password)
                .onSuccess { usuario ->
                    _state.update { it.copy(cargando = false) }
                    onExito(usuario)
                }
                .onFailure { e ->
                    _state.update { it.copy(cargando = false, error = e.message) }
                }
        }
    }
}