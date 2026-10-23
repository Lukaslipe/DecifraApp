package com.example.decifraappescolhaumtema.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.decifraappescolhaumtema.auth.SessaoUsuario
import com.example.decifraappescolhaumtema.data.BancoPalavras
import com.example.decifraappescolhaumtema.data.dal.UsuarioDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Resultado {
    CERTA,
    PRESENTE,
    AUSENTE,
    VAZIA
}

enum class EstadoJogo {
    JOGANDO,
    VENCEU,
    PERDEU
}

class JogoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UsuarioDao(application)

    private val tamanho = BancoPalavras.TAMANHO_PALAVRA
    private val maxTentativas = 6

    private var palavraSecreta: String = ""

    var tema by mutableStateOf("")
        private set

    var tentativas by mutableStateOf(List(maxTentativas) { List(tamanho) { ' ' } })
        private set

    var resultados by mutableStateOf(
        List(maxTentativas) { List(tamanho) { Resultado.VAZIA } }
    )
        private set

    var linhaAtual by mutableStateOf(0)
        private set

    var colunaAtual by mutableStateOf(0)
        private set

    var estado by mutableStateOf(EstadoJogo.JOGANDO)
        private set

    var pontos by mutableStateOf(0)
        private set

    var mensagem by mutableStateOf<String?>(null)

    val palavraCerta: String get() = palavraSecreta

    fun iniciar(tema: String) {
        this.tema = tema
        palavraSecreta = BancoPalavras.sortear(tema)
        tentativas = List(maxTentativas) { List(tamanho) { ' ' } }
        resultados = List(maxTentativas) { List(tamanho) { Resultado.VAZIA } }
        linhaAtual = 0
        colunaAtual = 0
        estado = EstadoJogo.JOGANDO
        pontos = 0
        mensagem = null
    }

    fun digitar(letra: Char) {
        if (estado != EstadoJogo.JOGANDO) return
        if (colunaAtual >= tamanho) return

        val novaLinha = tentativas[linhaAtual].toMutableList()
        novaLinha[colunaAtual] = letra
        tentativas = tentativas.toMutableList().also { it[linhaAtual] = novaLinha }
        colunaAtual++
        mensagem = null
    }

    fun apagar() {
        if (estado != EstadoJogo.JOGANDO) return
        if (colunaAtual <= 0) return

        colunaAtual--
        val novaLinha = tentativas[linhaAtual].toMutableList()
        novaLinha[colunaAtual] = ' '
        tentativas = tentativas.toMutableList().also { it[linhaAtual] = novaLinha }
        mensagem = null
    }

    fun confirmar() {
        if (estado != EstadoJogo.JOGANDO) return
        if (colunaAtual < tamanho) {
            mensagem = "Complete as $tamanho letras."
            return
        }

        val tentativa = tentativas[linhaAtual].joinToString("")
        val resultadoLinha = avaliar(tentativa, palavraSecreta)
        resultados = resultados.toMutableList().also { it[linhaAtual] = resultadoLinha }

        if (tentativa == palavraSecreta) {
            estado = EstadoJogo.VENCEU
            pontos = calcularPontos(linhaAtual)
            salvarPontos(pontos)
        } else if (linhaAtual == maxTentativas - 1) {
            estado = EstadoJogo.PERDEU
            pontos = 0
        } else {
            linhaAtual++
            colunaAtual = 0
        }
    }

    private fun avaliar(tentativa: String, secreta: String): List<Resultado> {
        val resultado = MutableList(tamanho) { Resultado.AUSENTE }
        val restantes = HashMap<Char, Int>()

        for (i in 0 until tamanho) {
            if (tentativa[i] == secreta[i]) {
                resultado[i] = Resultado.CERTA
            } else {
                restantes[secreta[i]] = (restantes[secreta[i]] ?: 0) + 1
            }
        }

        for (i in 0 until tamanho) {
            if (resultado[i] == Resultado.CERTA) continue
            val letra = tentativa[i]
            val sobra = restantes[letra] ?: 0
            if (sobra > 0) {
                resultado[i] = Resultado.PRESENTE
                restantes[letra] = sobra - 1
            }
        }

        return resultado
    }

    private fun calcularPontos(linha: Int): Int {
        return (maxTentativas - linha) * 10
    }

    private fun salvarPontos(pontos: Int) {
        val id = SessaoUsuario.usuarioId ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.somarPontos(id, pontos)
            }
        }
    }
}
