package com.example.decifraappescolhaumtema.ui.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class LocalizacaoViewModel(application: Application): AndroidViewModel(application) {
    var localizacao by mutableStateOf<Location?>(null)
        private set
    var carregando by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    // Variável para a bandeira
    var bandeira by mutableStateOf("🏳️")
        private set

    private var consulta: CancellationTokenSource? = null

    @SuppressLint("MissingPermission")
    fun buscar(autorizado: Boolean){
        if (carregando) return
        val contexto = getApplication<Application>()
        localizacao = null
        mensagem = null

        if(!autorizado || ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            informarPermissaoNegada()
            return
        }
        if (GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(contexto) != ConnectionResult.SUCCESS
        ){
            mensagem = "Este recurso precisa de permissão"
            return
        }
        val sinal = CancellationTokenSource()
        consulta = sinal
        carregando = true

        val pedido = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setMaxUpdateAgeMillis(10_000L)
            .setDurationMillis(20_000L)
            .build()

        try {
            LocationServices.getFusedLocationProviderClient(contexto)
                .getCurrentLocation(pedido, sinal.token)
                .addOnCompleteListener { tarefa ->
                    if (consulta != sinal) return@addOnCompleteListener
                    consulta = null
                    carregando = false
                    when{
                        tarefa.isSuccessful -> {
                            localizacao = tarefa.result
                            if (localizacao == null) {
                                mensagem = "Localização indisponível"
                            } else {
                                obterBandeira(localizacao!!.latitude, localizacao!!.longitude)
                            }
                        }
                        tarefa.exception is SecurityException -> informarPermissaoNegada()
                        else-> mensagem = "Não foi possível acessar a localização"
                    }
                }
        }catch (_: Exception){
            cancelar()
            mensagem = "Não foi possível consultar a localização"
        }
    }

    @Suppress("DEPRECATION")
    private fun obterBandeira(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val enderecos = geocoder.getFromLocation(lat, lon, 1)

                if (!enderecos.isNullOrEmpty()) {
                    val codigoPais = enderecos[0].countryCode
                    if (codigoPais != null && codigoPais.length == 2) {
                        val primeiraLetra = Character.codePointAt(codigoPais, 0) - 0x41 + 0x1F1E6
                        val segundaLetra = Character.codePointAt(codigoPais, 1) - 0x41 + 0x1F1E6
                        bandeira = String(Character.toChars(primeiraLetra)) + String(Character.toChars(segundaLetra))
                    }
                }
            } catch (_: Exception) {
                // Se falhar (ex: sem internet), mantém a bandeira padrão sem crashar
            }
        }
    }

    private fun informarPermissaoNegada(){
        localizacao = null
        mensagem = "Permissão negada."
    }

    fun cancelar(){
        val anterior = consulta
        anterior?.cancel()
        carregando = false
    }

    override fun onCleared() {
        cancelar()
        super.onCleared()
    }
}