package com.example.decifraappescolhaumtema.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.decifraappescolhaumtema.data.local.AppDatabase
import com.example.decifraappescolhaumtema.data.model.Usuario

internal class SenhaSalva(val hash: ByteArray, val salt: ByteArray)

internal class UsuarioDao(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(
        nome: String,
        senha: ByteArray,
        salt: ByteArray
    ) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("senha", senha)
                put("salt", salt)
            }
            db.writableDatabase.insertOrThrow("usuarios", null, dados)
        }
    }

    fun buscarSenha(nome: String): SenhaSalva? {
        return AppDatabase(appContext).use { db ->
            db.readableDatabase.query(
                "usuarios",
                arrayOf("senha", "salt"),
                "nome = ?",
                arrayOf(nome),
                null, null, null
            ).use { cursor ->
                if (cursor.moveToFirst()) SenhaSalva(
                    cursor.getBlob(0),
                    cursor.getBlob(1)
                ) else null
            }
        }
    }

    fun listar(): List<Usuario> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios",
            arrayOf("id", "nome"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        Usuario(
                            cursor.getLong(0),
                            cursor.getString(1)
                        )
                    )
                }
            }
        }
    }

    fun buscarPorId(id: Long): Usuario? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios",
            arrayOf("id", "nome"),
            "id = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) Usuario(
                cursor.getLong(0),
                cursor.getString(1)
            ) else null
        }
    }

    fun alterar(
        id: Long,
        nome: String,
        hash: ByteArray? = null,
        salt: ByteArray? = null
    ) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                if (hash != null) put("senha", hash)
                if (salt != null) put("salt", salt)
            }
            val alterados = db.writableDatabase.update(
                "usuarios",
                dados,
                "id = ?",
                arrayOf(id.toString())
            )
            require(alterados == 1) { "Erro ao alterar o usuário" }
        }
    }

    fun excluir(id: Long, usuarioAtualId: Long) {
        require(id != usuarioAtualId) { "Você não pode excluir seu próprio usuário" }

        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "usuarios",
                "id = ? AND id <> ?",
                arrayOf(id.toString(), usuarioAtualId.toString())
            )
            require(excluidos == 1) { "Usuário não pode ser excluído" }
        }
    }
}
