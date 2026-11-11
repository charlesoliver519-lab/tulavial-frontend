package com.example.sensors.location

import androidx.compose.runtime.Composable

@Composable
actual fun rememberSolicitarUbicacion(onEstado: (UbicacionEstado) -> Unit): () -> Unit = {
    onEstado(UbicacionEstado.Error("La ubicación solo está disponible en la app Android por ahora"))
}