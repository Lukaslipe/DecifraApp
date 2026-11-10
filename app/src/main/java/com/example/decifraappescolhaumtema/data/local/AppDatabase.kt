package com.example.decifraappescolhaumtema.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

        db.execSQL(
            """
            INSERT INTO categorias(id, nome) VALUES
            (1, 'Animais'),
            (2, 'Comidas'),
            (3, 'Profissões'),
            (4, 'Objetos')
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO palavras(texto, categoria_id) VALUES
            ('GATOS', 1), ('TIGRE', 1), ('ZEBRA', 1), ('POMBO', 1), ('URSOS', 1),
            ('GANSO', 1), ('PUMAS', 1), ('FOCAS', 1), ('LOBOS', 1), ('COBRA', 1),
            ('BURRO', 1), ('TATUS', 1), ('MICOS', 1), ('ONCAS', 1), ('RATOS', 1),
            ('ARROZ', 2), ('PIZZA', 2), ('SUSHI', 2), ('BOLOS', 2), ('MANGA', 2),
            ('LIMAO', 2), ('PERAS', 2), ('CARNE', 2), ('PENNE', 2), ('TORTA', 2),
            ('QUIBE', 2), ('PASTA', 2), ('MOLHO', 2), ('FRUTA', 2), ('DOCES', 2),
            ('AUTOR', 3), ('PADRE', 3), ('MAGOS', 3), ('POETA', 3), ('CAIXA', 3),
            ('JUIZA', 3), ('SERVO', 3), ('MUSAS', 3), ('CHEFE', 3), ('GUIAS', 3),
            ('COXAS', 3), ('VIGIA', 3), ('DAMAS', 3), ('REINA', 3), ('SABIO', 3),
            ('MESAS', 4), ('LIVRO', 4), ('COPOS', 4), ('PRATO', 4), ('FACAS', 4),
            ('GARFO', 4), ('VASOS', 4), ('CHAVE', 4), ('CAIXA', 4), ('PANOS', 4),
            ('SOFAS', 4), ('LUVAS', 4), ('VELAS', 4), ('BOLSA', 4), ('REMOS', 4)
            """.trimIndent()
        )
    }
}
