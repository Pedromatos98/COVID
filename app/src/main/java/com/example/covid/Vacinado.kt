package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Vacinado (var id: Long = -1, var dataAdmnistracao: String, var numeroAdmnistracoes: Long, var idCategoria: Long)  {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaVacinados.CAMPO_DATA_ADMNISTRACAO, dataAdmnistracao)
            put(TabelaVacinados.CAMPO_NUMERO_ADMNISTRACOES, numeroAdmnistracoes)
            put(TabelaVacinados.CAMPO_ID_PACIENTE, idCategoria)
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Vacinado {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colDataAdmn = cursor.getColumnIndex(TabelaVacinados.CAMPO_DATA_ADMNISTRACAO)
            val colNumeroAdm = cursor.getColumnIndex(TabelaVacinados.CAMPO_NUMERO_ADMNISTRACOES)
            val colIdPaciente = cursor.getColumnIndex(TabelaVacinados.CAMPO_ID_PACIENTE)

            val id = cursor.getLong(colId)
            val dataAdmn = cursor.getString(colDataAdmn)
            val numeroAdm = cursor.getLong(colNumeroAdm)
            val idPaciente = cursor.getLong(colIdPaciente)

            return Vacinado(id, dataAdmn, numeroAdm, idPaciente)
        }
    }
}
