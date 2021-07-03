package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaInfetados (db: SQLiteDatabase) {
    private val db: SQLiteDatabase = db

    fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,  $CAMPO_DATA_INFECAO DATE NOT NULL, $CAMPO_SINTOMAS TEXT NOT NULL, $CAMPO_ID_PACIENTE INTEGER NOT NULL,FOREIGN KEY($CAMPO_ID_PACIENTE) REFERENCES ${TabelaPacientes.NOME_TABELA})")
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
        val ultimaColuna = columns.size - 1

        var posColNomeCategoria = -1 // -1 indica que a coluna não foi pedida
        for (i in 0..ultimaColuna) {
            if (columns[i] == CAMPO_EXTERNO_NOME_PACIENTE) {
                posColNomeCategoria = i
                break
            }
        }

        if (posColNomeCategoria == -1) {
            return db.query(NOME_TABELA, columns, selection, selectionArgs, groupBy, having, orderBy)
        }

        var colunas = ""
        for (i in 0..ultimaColuna) {
            if (i > 0) colunas += ","

            colunas += if (i == posColNomeCategoria) {
                "${TabelaPacientes.NOME_TABELA}.${TabelaPacientes.CAMPO_NOME_PACIENTE} AS $CAMPO_EXTERNO_NOME_PACIENTE"
            } else {
                "${NOME_TABELA}.${columns[i]}"
            }
        }

        val tabelas = "$NOME_TABELA INNER JOIN ${TabelaPacientes.NOME_TABELA} ON ${TabelaPacientes.NOME_TABELA}.${BaseColumns._ID}=$CAMPO_ID_PACIENTE"

        var sql = "SELECT $colunas FROM $tabelas"

        if (selection != null) sql += " WHERE $selection"

        if (groupBy != null) {
            sql += " GROUP BY $groupBy"
            if (having != null) " HAVING $having"
        }

        if (orderBy != null) sql += " ORDER BY $orderBy"

        return db.rawQuery(sql, selectionArgs)
    }

    companion object {
        const val NOME_TABELA = "infetados"
        const val CAMPO_DATA_INFECAO = "data_infecao"
        const val CAMPO_SINTOMAS = "sintomas"
        const val CAMPO_ID_PACIENTE ="id_paciente"
        const val CAMPO_EXTERNO_NOME_PACIENTE ="nome_paciente"

        val TODAS_COLUNAS = arrayOf(BaseColumns._ID, CAMPO_DATA_INFECAO, CAMPO_SINTOMAS,
            CAMPO_ID_PACIENTE,CAMPO_EXTERNO_NOME_PACIENTE)
    }
}