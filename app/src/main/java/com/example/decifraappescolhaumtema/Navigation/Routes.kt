package com.example.decifraappescolhaumtema.Navigation

import kotlinx.serialization.Serializable

@Serializable
data object Inicial{

}

@Serializable
data object Login{

}

@Serializable
data class CriarConta(val origemUsuarios: Boolean = false)

@Serializable
data object Instrucoes{

}

@Serializable
data object EscolherTema{

}

@Serializable
data class Jogo(val tema: String){

}

@Serializable
data class Acerto(val palavra: String, val pontos: Int, val venceu: Boolean = true)

@Serializable
data object Usuarios

@Serializable
data class AlterarUsuario(val id: Long)
