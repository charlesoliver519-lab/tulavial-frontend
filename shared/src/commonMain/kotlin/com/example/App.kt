package com.example

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.domain.Rol
import com.example.navigation.Screen
import com.example.ui.home.*
import com.example.ui.login.LoginScreen

@Composable
fun App() {
    MaterialTheme {
        Surface {
            var screen by remember { mutableStateOf<Screen>(Screen.Login) }
            val logout = { screen = Screen.Login }

            when (val s = screen) {
                Screen.Login -> LoginScreen(onLoginExitoso = { usuario ->
                    screen = when (Rol.from(usuario.rol)) {
                        Rol.ADMINISTRADOR -> Screen.Administrador(usuario)
                        Rol.SUPERVISOR -> Screen.Supervisor(usuario)
                        else -> Screen.Ciudadano(usuario)
                    }
                })
                is Screen.Ciudadano -> CiudadanoScreen(s.usuario, logout)
                is Screen.Administrador -> AdministradorScreen(s.usuario, logout)
                is Screen.Supervisor -> SupervisorScreen(s.usuario, logout)
            }
        }
    }
}