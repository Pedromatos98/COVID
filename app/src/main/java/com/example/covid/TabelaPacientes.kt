package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaPacientes (db: SQLiteDatabase) {
    private val db: SQLiteDatabase = db
    fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $CAMPO_NOME_PACIENTE TEXT NOT NULL, $CAMPO_NUMERO_UTENTE INTEGER NOT NULL, $CAMPO_DATA_NASCIMENTO DATE NOT NULl,$CAMPO_CONTACTO TEXT NOT NULL )")
    }
    fun insert(values: ContentValues): Long {
        return db.insert(NOME_TABELA, null, values)
    }

    fun update(values: ContentValues, whereClause: String, whereArgs: Array<String>): Int {
        return db.update(NOME_TABELA, values, whereClause, whereArgs)
    }

    fun delete(whereClause: String, whereArgs: Array<String>): Int {
        return db.delete(NOME_TABELA, whereClause, whereArgs)
    }

    fun query(
        columns: Array<String>,
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): Cursor? {
        return db.query(NOME_TABELA, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    companion object {
        const val NOME_TABELA = "Pacientes"
        const val CAMPO_NOME_PACIENTE = "nome_paciente"
        const val CAMPO_NUMERO_UTENTE = "numero_utente"
        const val CAMPO_DATA_NASCIMENTO = "data_nascimento"
        const val CAMPO_CONTACTO = "contacto"

        val TODAS_COLUNAS = arrayOf(BaseColumns._ID, CAMPO_NOME_PACIENTE, CAMPO_NUMERO_UTENTE,
            CAMPO_DATA_NASCIMENTO, CAMPO_CONTACTO)
    }
}
}