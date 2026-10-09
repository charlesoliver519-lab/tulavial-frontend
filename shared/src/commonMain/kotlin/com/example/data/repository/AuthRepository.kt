package com.example.data.repository

import com.example.data.model.*
import com.example.data.remote.AuthApi

class AuthRepository(private val api: AuthApi = AuthApi()) {

    suspend fun login(usuario: String, password: String): Result<UsuarioDto> =
        try {
            val resp = api.login(LoginRequest(usuario, password))
            if (resp.exito && resp.usuario != null) Result.success(resp.usuario)
            else Result.failure(Exception(resp.mensaje))
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar con el servidor"))
        }
}