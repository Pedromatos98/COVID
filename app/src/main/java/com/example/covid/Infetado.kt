package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Infetado(var id: Long = -1, var dataInfecao: String, var sintomas: Long, var idPaciente: Long)  {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaInfetados.CAMPO_DATA_INFECAO, dataInfecao)
            put(TabelaInfetados.CAMPO_SINTOMAS, sintomas)
            put(TabelaInfetados.CAMPO_ID_PACIENTE, idPaciente)
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Vacinado {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colDataInfecao = cursor.getColumnIndex(TabelaInfetados.CAMPO_DATA_INFECAO)
            val colSintomas = cursor.getColumnIndex(TabelaInfetados.CAMPO_SINTOMAS)
            val colIdPaciente = cursor.getColumnIndex(TabelaInfetados.CAMPO_ID_PACIENTE)

            val id = cursor.getLong(colId)
            val dataAdmn = cursor.getString(colDataInfecao)
            val numeroAdm = cursor.getLong(colSintomas)
            val idPaciente = cursor.getLong(colIdPaciente)

            return Vacinado(id, dataAdmn, numeroAdm, idPaciente)
        }
    }
}
