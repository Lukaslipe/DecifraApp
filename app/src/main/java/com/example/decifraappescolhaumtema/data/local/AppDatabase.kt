package com.example.decifraappescolhaumtema.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.decifraappescolhaumtema.data.BancoPalavras
import java.io.File

internal class AppDatabase(context: Context) : SQLiteOpenHelper(
    context.applicationContext,
    File(context.noBackupFilesDir, "aula_sqlite.db").absolutePath,
    null,
    3
) {
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuarios(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL UNIQUE CHECK(length(nome) BETWEEN 3 AND 100),
            senha BLOB NOT NULL,
            salt BLOB NOT NULL,
            pontos INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
        criarTabelasCatalogo(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE usuarios ADD COLUMN pontos INTEGER NOT NULL DEFAULT 0")
        }
        if (oldVersion < 3) {
            criarTabelasCatalogo(db)
        }
    }

    private fun criarTabelasCatalogo(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE categorias(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL UNIQUE COLLATE NOCASE CHECK(length(nome) BETWEEN 2 AND 30)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE palavras(
            id INTEGER PRIMARY KEY,
            texto TEXT NOT NULL CHECK(length(texto) = 5),
            categoria_id INTEGER NOT NULL REFERENCES categorias(id) ON DELETE CASCADE,
            UNIQUE(texto, categoria_id)
            )
            """.trimIndent()
        )

        BancoPalavras.padrao.forEach { (nome, palavras) ->
            val categoriaId = db.insertOrThrow(
                "categorias", null,
                ContentValues().apply { put("nome", nome) }
            )
            palavras.forEach { texto ->
                db.insertWithOnConflict(
                    "palavras", null,
                    ContentValues().apply {
                        put("texto", texto)
                        put("categoria_id", categoriaId)
                    },
                    SQLiteDatabase.CONFLICT_IGNORE
                )
            }
        }
    }
}
