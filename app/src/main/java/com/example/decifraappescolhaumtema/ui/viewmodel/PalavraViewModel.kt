package com.example.decifraappescolhaumtema.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.decifraappescolhaumtema.ui.screens.PalavraItem

class PalavraViewModel : ViewModel() {

    var palavra by mutableStateOf("")
        private set

    var categoria by mutableStateOf("")
        private set

    var palavrasList by mutableStateOf(listOf<PalavraItem>())
        private set

    var categoriasList by mutableStateOf(listOf("Animais", "Objetos", "Comidas", "Cores"))
        private set

    fun mudarPalavra(novoTexto: String) {
        palavra = novoTexto
    }

    fun mudarCategoria(novaCategoria: String) {
        categoria = novaCategoria
    }

    fun carregarPalavras() {
    }

    fun salvarPalavra() {
        if (palavra.isNotBlank() && categoria.isNotBlank()) {
            val novaItem = PalavraItem(
                id = System.currentTimeMillis(),
                texto = palavra,
                categoria = categoria,
            )
            palavrasList = palavrasList + novaItem

            palavra = ""
            categoria = ""
        }
    }

    fun excluirPalavra(id: Long) {
        palavrasList = palavrasList.filter { it.id != id }
    }
}