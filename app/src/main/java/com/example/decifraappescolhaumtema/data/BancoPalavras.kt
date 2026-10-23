package com.example.decifraappescolhaumtema.data

object BancoPalavras {

    const val TAMANHO_PALAVRA = 5

    private val palavrasPorTema: Map<String, List<String>> = mapOf(
        "Animais" to listOf(
            "GATOS", "TIGRE", "ZEBRA", "POMBO", "URSOS",
            "GANSO", "PUMAS", "FOCAS", "LOBOS", "COBRA",
            "BURRO", "TATUS", "MICOS", "ONCAS", "RATOS"
        ),
        "Comidas" to listOf(
            "ARROZ", "PIZZA", "SUSHI", "BOLOS", "MANGA",
            "LIMAO", "PERAS", "CARNE", "PENNE", "TORTA",
            "QUIBE", "PASTA", "MOLHO", "FRUTA", "DOCES"
        ),
        "Profissões" to listOf(
            "AUTOR", "PADRE", "MAGOS", "POETA", "CAIXA",
            "JUIZA", "SERVO", "MUSAS", "CHEFE", "GUIAS",
            "COXAS", "VIGIA", "DAMAS", "REINA", "SABIO"
        ),
        "Objetos" to listOf(
            "MESAS", "LIVRO", "COPOS", "PRATO", "FACAS",
            "GARFO", "VASOS", "CHAVE", "CAIXA", "PANOS",
            "SOFAS", "LUVAS", "VELAS", "BOLSA", "REMOS"
        )
    )

    fun sortear(tema: String): String {
        val lista = palavrasPorTema[tema] ?: palavrasPorTema.values.first()
        return lista.random()
    }
}
