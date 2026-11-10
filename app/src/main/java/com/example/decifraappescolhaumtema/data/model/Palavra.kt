package com.example.decifraappescolhaumtema.data.model

data class Palavra(
    val id: Long,
    val texto: String,
    val categoriaId: Long,
    val categoria: String
)
