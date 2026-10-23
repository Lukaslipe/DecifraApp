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
import com.example.decifraappescolhaumtema.data.dal.RankingUsuario
import com.example.decifraappescolhaumtema.data.dal.UsuarioDao
import com.example.decifraappescolhaumtema.data.model.Usuario
import com.example.decifraappescolhaumtema.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)

    val usuarioAtivoId: Long? get() = SessaoUsuario.usuarioId

    var usuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var ranking by mutableStateOf<List<RankingUsuario>>(emptyList())
        private set
    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var senha by mutableStateOf("")
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    private fun executar(op: suspend () -> Unit) {
        if (carregando || salvo) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                op()
            } catch (e: CancellationException) {
                throw e
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

    fun listar() = executar {
        val lista = withContext(Dispatchers.IO) { dao.listar() }
        usuarios = lista
    }

    fun carregarRanking() = executar {
        val lista = withContext(Dispatchers.IO) { dao.listarRanking() }
        ranking = lista
    }

    fun buscarPorId(id: Long) {
        if (usuario?.id == id) return

        executar {
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            usuario = requireNotNull(encontrado) { "Usuário não encontrado." }
            nome = encontrado.nome
            senha = ""
        }
    }

    fun mudarNome(valor: String) {
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun mudarSenha(valor: String) {
        if (!carregando && valor.length <= 128) senha = valor
    }

    fun salvar() = executar {
        val usuarioSelecionado = requireNotNull(usuario) { "Selecione um usuário." }

        val nomeLimpo = nome.trim()
        val senhaLimpa = senha.trim()

        withContext(Dispatchers.IO) {
            if (senhaLimpa.isEmpty()) {
                require(nomeLimpo.length in 3..100) {
                    "Informe um usuário entre 3 e 100 caracteres."
                }
                dao.alterar(usuarioSelecionado.id, nomeLimpo)
            } else {
                CadastroValidator.validar(nomeLimpo, senhaLimpa)
                val salt = AuthManager.criarSalt()
                val hash = AuthManager.gerarHash(senhaLimpa, salt)
                try {
                    dao.alterar(usuarioSelecionado.id, nomeLimpo, hash, salt)
                } finally {
                    hash.fill(0)
                }
            }
        }

        salvo = true
    }

    fun excluir(id: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()

        require(id != atual) { "Você não pode excluir seu próprio usuário." }

        withContext(Dispatchers.IO) { dao.excluir(id, atual) }

        usuarios = usuarios.filterNot { it.id == id }
        mensagem = "Usuário excluído."
    }

    fun consumirSalvo() {
        salvo = false
    }
}
