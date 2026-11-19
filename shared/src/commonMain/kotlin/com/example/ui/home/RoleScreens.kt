package com.example.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.UsuarioDto
import com.example.sensors.location.UbicacionCard

@Composable
private fun RoleScaffold(
    titulo: String,
    usuario: UsuarioDto,
    funciones: List<String>,
    onLogout: () -> Unit,
    extra: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text("Sesión: ${usuario.nombre} (${usuario.rol})")
        HorizontalDivider()
        funciones.forEach { Text("• $it") }
        extra()
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun CiudadanoScreen(usuario: UsuarioDto, onLogout: () -> Unit) = RoleScaffold(
    titulo = "Inicio del ciudadano",
    usuario = usuario,
    funciones = listOf("Reportar bache o falla de alumbrado", "Ver mis reportes en el mapa", "Consultar estatus"),
    onLogout = onLogout,
    extra = { UbicacionCard() }
)

private data class ReporteAdmin(
    val folio: String,
    val tipo: String,
    val ubicacion: String,
    val estado: String,
    val cuadrilla: String? = null
)

@Composable
fun AdministradorScreen(usuario: UsuarioDto, onLogout: () -> Unit) {
    var filtro by remember { mutableStateOf("Todos") }
    var reportes by remember {
        mutableStateOf(
            listOf(
                ReporteAdmin("#101", "Bache", "Blvd. Nuevo Hidalgo, Tulancingo", "Pendiente", null),
                ReporteAdmin("#102", "Semáforo", "Crucero Acatlán - Centro", "En Proceso", "Cuadrilla Vial #1"),
                ReporteAdmin("#103", "Alumbrado", "Col. Jardines del Sur", "Pendiente", null)
            )
        )
    }

    val filtrados = if (filtro == "Todos") reportes else reportes.filter { it.tipo == filtro }

    RoleScaffold(
        titulo = "Panel administrativo",
        usuario = usuario,
        funciones = listOf("Mapa de calor de reportes", "Priorización de reportes", "Cambiar estatus"),
        onLogout = onLogout,
        extra = {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Tarjetas de métricas (KPIs)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiResumen(
                    titulo = "Pendientes",
                    cantidad = reportes.count { it.estado == "Pendiente" }.toString(),
                    fondo = Color(0xFFFFEBEE),
                    texto = Color(0xFFC62828),
                    modifier = Modifier.weight(1f)
                )
                KpiResumen(
                    titulo = "En Proceso",
                    cantidad = reportes.count { it.estado == "En Proceso" }.toString(),
                    fondo = Color(0xFFFFF8E1),
                    texto = Color(0xFFEF6C00),
                    modifier = Modifier.weight(1f)
                )
                KpiResumen(
                    titulo = "Resueltos",
                    cantidad = reportes.count { it.estado == "Resuelto" }.toString(),
                    fondo = Color(0xFFE8F5E9),
                    texto = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
            }

            // 2. Botones de filtro rápido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Todos", "Bache", "Alumbrado", "Semáforo").forEach { opcion ->
                    FilterChip(
                        selected = filtro == opcion,
                        onClick = { filtro = opcion },
                        label = { Text(opcion, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            // 3. Tarjetas interactivas de reportes
            filtrados.forEach { rep ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${rep.folio} • ${rep.tipo}", fontWeight = FontWeight.Bold)
                            Text(
                                text = rep.estado,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = when (rep.estado) {
                                    "Pendiente" -> Color(0xFFC62828)
                                    "En Proceso" -> Color(0xFFEF6C00)
                                    else -> Color(0xFF2E7D32)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(rep.ubicacion, style = MaterialTheme.typography.bodySmall)

                        if (rep.cuadrilla != null) {
                            Text(
                                text = "Asignado: ${rep.cuadrilla}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (rep.estado != "Resuelto") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    reportes = reportes.map {
                                        if (it.folio == rep.folio) {
                                            if (it.estado == "Pendiente") {
                                                it.copy(estado = "En Proceso", cuadrilla = "Cuadrilla Tulancingo #1")
                                            } else {
                                                it.copy(estado = "Resuelto")
                                            }
                                        } else it
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (rep.estado == "Pendiente") "Asignar cuadrilla" else "Marcar como resuelto")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun KpiResumen(
    titulo: String,
    cantidad: String,
    fondo: Color,
    texto: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = fondo),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(cantidad, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = texto)
            Text(titulo, style = MaterialTheme.typography.labelSmall, color = texto)
        }
    }
}

@Composable
fun SupervisorScreen(usuario: UsuarioDto, onLogout: () -> Unit) = RoleScaffold(
    titulo = "Vista del supervisor",
    usuario = usuario,
    funciones = listOf("Reportes asignados a la cuadrilla", "Subir evidencia de reparación"),
    onLogout = onLogout
)