package com.example.decifraappescolhaumtema.validation

import com.example.decifraappescolhaumtema.data.BancoPalavras
import java.text.Normalizer

object CatalogoValidator {
    private val MARCAS_DE_ACENTO = Regex("\\p{M}+")

    fun categoria(nome: String): String {
        val limpo = nome.trim()
        require(limpo.length in 2..30) {
            "Informe uma categoria entre 2 e 30 caracteres."
        }
        return limpo
    }

    fun palavra(texto: String): String {
        val limpo = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
            .replace(MARCAS_DE_ACENTO, "")
            .uppercase()
        require(limpo.length == BancoPalavras.TAMANHO_PALAVRA && limpo.all { it in 'A'..'Z' }) {
            "A palavra deve ter exatamente ${BancoPalavras.TAMANHO_PALAVRA} letras, sem números ou símbolos."
        }
        return limpo
    }
}
