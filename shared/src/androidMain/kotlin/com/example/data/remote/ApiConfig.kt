package com.example.data.remote

actual fun baseUrl(): String =
    if (USE_REMOTE_SERVER) REMOTE_URL else "http://10.0.2.2:8080"