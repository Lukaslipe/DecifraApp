package com.example.decifraappescolhaumtema.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.decifraappescolhaumtema.data.local.AppDatabase
import com.example.decifraappescolhaumtema.data.model.Palavra

internal class PalavraDao(context: Context) {
    private val appContext = context.applicationContext

    fun listar(): List<Palavra> = AppDatabase(appContext).use { db ->
        db.readableDatabase.rawQuery(
            """
            SELECT p.id, p.texto, p.categoria_id, c.nome
            FROM palavras p
            JOIN categorias c ON c.id = p.categoria_id
            ORDER BY c.nome COLLATE NOCASE, p.texto
            """.trimIndent(),
            null
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        Palavra(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getLong(2),
                            cursor.getString(3)
                        )
                    )
                }
            }
        }
    }

    fun criar(texto: String, categoriaId: Long) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("texto", texto)
                put("categoria_id", categoriaId)
            }
            db.writableDatabase.insertOrThrow("palavras", null, dados)
        }
    }

    fun alterar(id: Long, texto: String, categoriaId: Long) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("texto", texto)
                put("categoria_id", categoriaId)
            }
            val alterados = db.writableDatabase.update(
                "palavras", dados, "id = ?", arrayOf(id.toString())
            )
            require(alterados == 1) { "Palavra não encontrada." }
        }
    }

    fun excluir(id: Long) {
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "palavras", "id = ?", arrayOf(id.toString())
            )
            require(excluidos == 1) { "Palavra não encontrada." }
        }
    }

    fun sortear(tema: String): String? = AppDatabase(appContext).use { db ->
        db.readableDatabase.rawQuery(
            """
            SELECT p.texto
            FROM palavras p
            JOIN categorias c ON c.id = p.categoria_id
            WHERE c.nome = ?
            ORDER BY RANDOM()
            LIMIT 1
            """.trimIndent(),
            arrayOf(tema)
        ).use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    }
}
