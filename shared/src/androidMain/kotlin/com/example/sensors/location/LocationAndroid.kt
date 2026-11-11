package com.example.sensors.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@Composable
actual fun rememberSolicitarUbicacion(onEstado: (UbicacionEstado) -> Unit): () -> Unit {
    val context = LocalContext.current
    val estadoActual by rememberUpdatedState(onEstado)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        if (permisos.values.any { it }) obtenerUbicacion(context) { estadoActual(it) }
        else estadoActual(UbicacionEstado.PermisoDenegado)
    }

    return remember(context) {
        {
            if (tienePermiso(context)) {
                obtenerUbicacion(context) { estadoActual(it) }
            } else {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
}

private fun tienePermiso(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

@SuppressLint("MissingPermission")
private fun obtenerUbicacion(context: Context, onEstado: (UbicacionEstado) -> Unit) {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!LocationManagerCompat.isLocationEnabled(manager)) {
        onEstado(UbicacionEstado.Error("Activa la ubicación (GPS) del teléfono"))
        return
    }
    onEstado(UbicacionEstado.Buscando)
    LocationServices.getFusedLocationProviderClient(context)
        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { loc ->
            if (loc != null) {
                onEstado(
                    UbicacionEstado.Obtenida(
                        Ubicacion(
                            latitud = loc.latitude,
                            longitud = loc.longitude,
                            precisionMetros = if (loc.hasAccuracy()) loc.accuracy else null
                        )
                    )
                )
            } else {
                onEstado(UbicacionEstado.Error("No se obtuvo la ubicación. Sal a un lugar abierto e inténtalo de nuevo"))
            }
        }
        .addOnFailureListener { e ->
            onEstado(UbicacionEstado.Error(e.message ?: "Error al obtener la ubicación"))
        }
}