package com.example.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UsuarioDto
import androidx.compose.ui.text.style.TextAlign

/**
 * Oliver
 */
@Composable
fun LoginScreen(
    onLoginExitoso: (UsuarioDto) -> Unit,
    viewModel: LoginViewModel = viewModel { LoginViewModel() }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("TulaVial", style = MaterialTheme.typography.headlineLarge)
        Text("Reportar en 30 segundos, reparar más rápido",
            style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = state.usuario,
            onValueChange = viewModel::onUsuarioChange,
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { viewModel.login(onLoginExitoso) },
            enabled = !state.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.cargando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Iniciar sesión")
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = "Juan Carlos Oliver Merida",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}