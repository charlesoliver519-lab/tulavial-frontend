package com.example.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.data.model.*

class AuthApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun login(request: LoginRequest): LoginResponse =
        client.post("${baseUrl()}/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}