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
data class PiadaResponse(val piada: String)

interface PiadaService {
    @GET("piada")
    suspend fun buscarPiada(): PiadaResponse
}

object PiadaApi {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val json = Json { ignoreUnknownKeys = true }

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("ngrok-skip-browser-warning", "true")
                    .build()
            )
        }
        .build()

    val service: PiadaService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PiadaService::class.java)
    }
}
