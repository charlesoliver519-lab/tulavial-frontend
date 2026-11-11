package com.example.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.UsuarioDto
import com.example.sensors.location.UbicacionCard

@Composable
private fun RoleScaffold(
    titulo: String,
    usuario: UsuarioDto,
    funciones: List<String>,
    onLogout: () -> Unit,
    extra: @Composable ColumnScope.() -> Unit = {}      // NUEVO
) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text("Sesión: ${usuario.nombre} (${usuario.rol})")
        HorizontalDivider()
        funciones.forEach { Text("• $it") }
        extra()                                          // NUEVO
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
    }
}

@Composable
fun CiudadanoScreen(usuario: UsuarioDto, onLogout: () -> Unit) = RoleScaffold(
    titulo = "Inicio del ciudadano",
    usuario = usuario,
    funciones = listOf("Reportar bache o falla de alumbrado", "Ver mis reportes en el mapa", "Consultar estatus"),
    onLogout = onLogout,
    extra = { UbicacionCard() }                          // NUEVO
)

@Composable
fun AdministradorScreen(usuario: UsuarioDto, onLogout: () -> Unit) = RoleScaffold(
    titulo = "Panel administrativo",
    usuario = usuario,
    funciones = listOf("Mapa de calor de reportes", "Priorización de reportes", "Cambiar estatus"),
    onLogout = onLogout
)

@Composable
fun SupervisorScreen(usuario: UsuarioDto, onLogout: () -> Unit) = RoleScaffold(
    titulo = "Vista del supervisor",
    usuario = usuario,
    funciones = listOf("Reportes asignados a la cuadrilla", "Subir evidencia de reparación"),
    onLogout = onLogout
)