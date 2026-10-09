package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val usuario: String,
    val password: String
)

@Serializable
data class UsuarioDto(
    val id: Int,
    val nombre: String,
    val rol: String
)

@Serializable
data class LoginResponse(
    val exito: Boolean,
    val mensaje: String,
    val usuario: UsuarioDto? = null
)