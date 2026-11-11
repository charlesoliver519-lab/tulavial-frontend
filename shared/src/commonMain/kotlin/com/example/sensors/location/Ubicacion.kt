package com.example.sensors.location

import androidx.compose.runtime.Composable

data class Ubicacion(
    val latitud: Double,
    val longitud: Double,
    val precisionMetros: Float?
)

sealed interface UbicacionEstado {
    data object Inactivo : UbicacionEstado
    data object Buscando : UbicacionEstado
    data class Obtenida(val ubicacion: Ubicacion) : UbicacionEstado
    data object PermisoDenegado : UbicacionEstado
    data class Error(val mensaje: String) : UbicacionEstado
}

/** Devuelve una función que pide el permiso (si falta) y obtiene la ubicación actual. */
@Composable
expect fun rememberSolicitarUbicacion(onEstado: (UbicacionEstado) -> Unit): () -> Unit
