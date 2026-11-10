package com.example.decifraappescolhaumtema.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.decifraappescolhaumtema.data.dal.CategoriaDao
import com.example.decifraappescolhaumtema.data.model.Categoria
import com.example.decifraappescolhaumtema.validation.CatalogoValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = CategoriaDao(application)

    var categorias by mutableStateOf<List<Categoria>>(emptyList())
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    val temasJogaveis: List<String>
        get() = categorias.filter { it.totalPalavras > 0 }.map { it.nome }

    private fun executar(op: suspend () -> Unit) {
        if (carregando) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                op()
            } catch (e: CancellationException) {
                throw e
            } catch (e: SQLiteConstraintException) {
                mensagem = "Essa categoria já existe."
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    private suspend fun recarregar() {
        categorias = withContext(Dispatchers.IO) { dao.listar() }
    }

    fun listar() = executar { recarregar() }

    fun criar(nome: String) = executar {
        val limpo = CatalogoValidator.categoria(nome)
        withContext(Dispatchers.IO) { dao.criar(limpo) }
        recarregar()
        salvo = true
    }

    fun alterar(id: Long, nome: String) = executar {
        val limpo = CatalogoValidator.categoria(nome)
        withContext(Dispatchers.IO) { dao.alterar(id, limpo) }
        recarregar()
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO) { dao.excluir(id) }
        recarregar()
        mensagem = "Categoria excluída."
    }

    fun limparMensagem() {
        mensagem = null
    }

    fun consumirSalvo() {
        salvo = false
    }
}
