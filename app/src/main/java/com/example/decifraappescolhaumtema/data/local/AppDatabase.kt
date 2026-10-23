package com.example.decifraappescolhaumtema.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

internal class AppDatabase(context: Context) : SQLiteOpenHelper(
    context.applicationContext,
    File(context.noBackupFilesDir, "aula_sqlite.db").absolutePath,
    null,
    2
) {
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE usuarios ADD COLUMN pontos INTEGER NOT NULL DEFAULT 0")
        }
    }
}
