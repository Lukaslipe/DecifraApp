package com.example.decifraappescolhaumtema.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.decifraappescolhaumtema.data.local.AppDatabase
import com.example.decifraappescolhaumtema.data.model.Categoria

internal class CategoriaDao(context: Context) {
    private val appContext = context.applicationContext

    fun listar(): List<Categoria> = AppDatabase(appContext).use { db ->
        db.readableDatabase.rawQuery(
            """
            SELECT c.id, c.nome, COUNT(p.id)
            FROM categorias c
            LEFT JOIN palavras p ON p.categoria_id = c.id
            GROUP BY c.id
            ORDER BY c.nome COLLATE NOCASE
            """.trimIndent(),
            null
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(Categoria(cursor.getLong(0), cursor.getString(1), cursor.getInt(2)))
                }
            }
        }
    }

    fun criar(nome: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply { put("nome", nome) }
            db.writableDatabase.insertOrThrow("categorias", null, dados)
        }
    }

    fun alterar(id: Long, nome: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply { put("nome", nome) }
            val alterados = db.writableDatabase.update(
                "categorias", dados, "id = ?", arrayOf(id.toString())
            )
            require(alterados == 1) { "Categoria não encontrada." }
        }
    }

    fun excluir(id: Long) {
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "categorias", "id = ?", arrayOf(id.toString())
            )
            require(excluidos == 1) { "Categoria não encontrada." }
        }
    }
}
