package com.example.decifraappescolhaumtema.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.decifraappescolhaumtema.data.dal.CategoriaDao
import com.example.decifraappescolhaumtema.data.dal.PalavraDao
import com.example.decifraappescolhaumtema.data.model.Categoria
import com.example.decifraappescolhaumtema.data.model.Palavra
import com.example.decifraappescolhaumtema.validation.CatalogoValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PalavraViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = PalavraDao(application)
    private val categoriaDao = CategoriaDao(application)

    var palavras by mutableStateOf<List<Palavra>>(emptyList())
        private set

    var categorias by mutableStateOf<List<Categoria>>(emptyList())
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

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
                mensagem = "Essa palavra já existe nessa categoria."
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
        val (listaPalavras, listaCategorias) = withContext(Dispatchers.IO) {
            dao.listar() to categoriaDao.listar()
        }
        palavras = listaPalavras
        categorias = listaCategorias
    }

    fun listar() = executar { recarregar() }

    fun criar(texto: String, categoriaId: Long?) = executar {
        val categoria = requireNotNull(categoriaId) { "Selecione uma categoria." }
        val limpa = CatalogoValidator.palavra(texto)
        withContext(Dispatchers.IO) { dao.criar(limpa, categoria) }
        recarregar()
        salvo = true
    }

    fun alterar(id: Long, texto: String, categoriaId: Long?) = executar {
        val categoria = requireNotNull(categoriaId) { "Selecione uma categoria." }
        val limpa = CatalogoValidator.palavra(texto)
        withContext(Dispatchers.IO) { dao.alterar(id, limpa, categoria) }
        recarregar()
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO) { dao.excluir(id) }
        recarregar()
        mensagem = "Palavra excluída."
    }

    fun limparMensagem() {
        mensagem = null
    }

    fun consumirSalvo() {
        salvo = false
    }
}
