package com.example.navigation

import com.example.data.model.UsuarioDto

sealed interface Screen {
    data object Login : Screen
    data class Ciudadano(val usuario: UsuarioDto) : Screen
    data class Administrador(val usuario: UsuarioDto) : Screen
    data class Supervisor(val usuario: UsuarioDto) : Screen
}