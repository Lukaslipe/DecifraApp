package com.example.decifraappescolhaumtema.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.decifraappescolhaumtema.auth.AuthManager
import com.example.decifraappescolhaumtema.auth.SessaoUsuario
import com.example.decifraappescolhaumtema.data.dal.UsuarioDao
import com.example.decifraappescolhaumtema.validation.CadastroValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)

    var carregando by mutableStateOf(false)
        private set
    var concluido by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)

    private fun executar(op: () -> Boolean) {
        if (carregando || concluido) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                concluido = withContext(Dispatchers.IO) { op() }
                if (!concluido) mensagem = "Usuário ou senha inválido."
            } catch (e: SQLiteConstraintException) {
                mensagem = "Usuário já cadastrado."
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    fun cadastrar(usuario: String, senha: String) = executar {
        val usuarioLimpo = usuario.trim()
        val senhaLimpa = senha.trim()

        CadastroValidator.validar(usuarioLimpo, senhaLimpa)

        val salt = AuthManager.criarSalt()
        val senhaHash = AuthManager.gerarHash(senhaLimpa, salt)
        try {
            dao.cadastrar(usuarioLimpo, senhaHash, salt)
            true
        } finally {
            senhaHash.fill(0)
        }
    }

    fun login(usuario: String, senha: String) = executar {
        require(usuario.length in 3..100 && senha.length in 6..128) {
            "Usuário ou senha inválido."
        }

        val usuarioLimpo = usuario.trim()
        val dados = dao.buscarSenha(usuarioLimpo)
        val senhaHash = dados?.hash ?: ByteArray(32)
        val salt = dados?.salt ?: ByteArray(16)

        try {
            val autenticado = AuthManager.validarSenha(senha, salt, senhaHash)
            if (dados != null && autenticado) {
                val id = dao.buscarIdPorNome(usuarioLimpo)
                if (id != null) SessaoUsuario.iniciar(id)
                id != null
            } else {
                false
            }
        } finally {
            senhaHash.fill(0)
        }
    }

    fun consumirResultado() {
        concluido = false
    }
}
