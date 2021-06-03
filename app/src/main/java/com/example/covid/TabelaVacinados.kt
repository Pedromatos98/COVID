package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaVacinados (db: SQLiteDatabase) {
    private val db: SQLiteDatabase = db
    fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,$CAMPO_DATA_ADMNISTRACAO DATE NOT NULL, $CAMPO_NUMERO_ADMNISTRACOES INTEGER NOT NULl,FOREIGN KEY($CAMPO_ID_PACIENTE) REFERENCES ${TabelaPacientes.NOME_TABELA})")
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
        const val NOME_TABELA = "Vacinados"
        const val CAMPO_DATA_ADMNISTRACAO = "data_admnistracao"
        const val CAMPO_NUMERO_ADMNISTRACOES = "numero_admnistracoes"
        const val CAMPO_ID_PACIENTE ="id_paciente"

        val TODAS_COLUNAS = arrayOf(BaseColumns._ID, CAMPO_DATA_ADMNISTRACAO,CAMPO_NUMERO_ADMNISTRACOES,CAMPO_ID_PACIENTE)
    }
}