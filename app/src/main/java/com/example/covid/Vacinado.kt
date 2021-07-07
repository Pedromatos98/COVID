package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Vacinado (var id: Long = -1, var dataAdmnistracao: String, var numeroAdmnistracoes: Long, var idPaciente: Long, var nomePaciente: String ?= null)  {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaVacinados.CAMPO_DATA_ADMNISTRACAO, dataAdmnistracao)
            put(TabelaVacinados.CAMPO_NUMERO_ADMNISTRACOES, numeroAdmnistracoes)
            put(TabelaVacinados.CAMPO_ID_PACIENTE, idPaciente)
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Vacinado {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colDataAdmn = cursor.getColumnIndex(TabelaVacinados.CAMPO_DATA_ADMNISTRACAO)
            val colNumeroAdm = cursor.getColumnIndex(TabelaVacinados.CAMPO_NUMERO_ADMNISTRACOES)
            val colIdPaciente = cursor.getColumnIndex(TabelaVacinados.CAMPO_ID_PACIENTE)
            val colNomePaciente= cursor.getColumnIndex(TabelaInfetados.CAMPO_EXTERNO_NOME_PACIENTE)

            val id = cursor.getLong(colId)
            val dataAdmn = cursor.getString(colDataAdmn)
            val numeroAdm = cursor.getLong(colNumeroAdm)
            val idPaciente = cursor.getLong(colIdPaciente)
            val nomePaciente = if (colNomePaciente != -1) cursor.getString(colNomePaciente) else null

            return Vacinado(id, dataAdmn, numeroAdm, idPaciente,nomePaciente)
        }
    }
}
