package com.example.domain

enum class Rol {
    CIUDADANO, ADMINISTRADOR, SUPERVISOR;

    companion object {
        fun from(valor: String): Rol? = entries.firstOrNull { it.name == valor }
    }
}