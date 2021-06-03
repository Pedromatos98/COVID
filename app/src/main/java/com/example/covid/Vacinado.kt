package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Vacinado (var id: Long = -1, var dataAdmn: String, var numero_admnistracoes: Long, var idCategoria: Long)  {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaVacinados.CAMPO_DATA_ADMNISTRACAO, dataAdmn)
            put(TabelaVacinados.CAMPO_NUMERO_ADMNISTRACOES, numero_admnistracoes)
            put(TabelaVacinados.CAMPO_ID_PACIENTE, idCategoria)
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Vacinado {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colDataAdmn = cursor.getColumnIndex(TabelaVacinados.CAMPO_DATA_ADMNISTRACAO)
            val colNumeroAdm = cursor.getColumnIndex(TabelaVacinados.CAMPO_NUMERO_ADMNISTRACOES)
            val colIdCateg = cursor.getColumnIndex(TabelaVacinados.CAMPO_ID_PACIENTE)

            val id = cursor.getLong(colId)
            val dataAdmn = cursor.getString(colDataAdmn)
            val NumeroAdm = cursor.getLong(colNumeroAdm)
            val idCateg = cursor.getLong(colIdCateg)

            return Vacinado(id, dataAdmn, NumeroAdm, idCateg)
        }
    }
}
