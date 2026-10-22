package com.example.decifraappescolhaumtema.auth

object AuthManager {
    data class  User(
        val usuario: String,
        val senha: String)
    private val usuarios = mutableListOf<User>()

    fun login(usuario: String, senha: String): Boolean{
        return usuarios.any { it.usuario == usuario && it.senha == senha }
    }

    fun cadastro(usuario: String, senha: String): Boolean{
        if(usuarios.any { it.usuario == usuario }) return false

        usuarios.add(User(usuario, senha))
        return true
    }
}