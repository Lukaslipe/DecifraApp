package com.example.decifraappescolhaumtema.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

@Serializable
data class Conselho(val id: Int, val advice: String)

@Serializable
data class ConselhoResponse(val slip: Conselho)

interface ConselhoService {
    @GET("advice")
    suspend fun buscarConselho(): ConselhoResponse
}

object ConselhoApi {
    private const val BASE_URL = "https://api.adviceslip.com/"

    private val json = Json { ignoreUnknownKeys = true }

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    val service: ConselhoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ConselhoService::class.java)
    }
}
