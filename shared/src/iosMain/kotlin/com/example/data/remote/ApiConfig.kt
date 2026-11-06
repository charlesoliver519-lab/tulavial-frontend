package com.example.data.remote

actual fun baseUrl(): String =
    if (USE_REMOTE_SERVER) REMOTE_URL else "http://localhost:8080"