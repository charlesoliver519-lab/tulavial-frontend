package com.example.sensors.location

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UbicacionCard() {
    var estado by remember { mutableStateOf<UbicacionEstado>(UbicacionEstado.Inactivo) }
    val solicitar = rememberSolicitarUbicacion { estado = it }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Mi ubicación (GPS)", style = MaterialTheme.typography.titleMedium)

            when (val e = estado) {
                UbicacionEstado.Inactivo ->
                    Text("Pulsa el botón para obtener tu ubicación actual.")
                UbicacionEstado.Buscando ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text("Buscando señal GPS…")
                    }
                is UbicacionEstado.Obtenida -> {
                    Text("Latitud: ${e.ubicacion.latitud}")
                    Text("Longitud: ${e.ubicacion.longitud}")
                    e.ubicacion.precisionMetros?.let { Text("Precisión: ±${it.toInt()} m") }
                }
                UbicacionEstado.PermisoDenegado ->
                    Text("Permiso de ubicación denegado. Actívalo en Ajustes del teléfono.", color = MaterialTheme.colorScheme.error)
                is UbicacionEstado.Error ->
                    Text(e.mensaje, color = MaterialTheme.colorScheme.error)
            }

            Button(onClick = solicitar, enabled = estado != UbicacionEstado.Buscando) {
                Text("Obtener mi ubicación")
            }
        }
    }
}