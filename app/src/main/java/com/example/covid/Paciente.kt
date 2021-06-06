package com.example.covid

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Paciente (var id: Long = -1, var nomePaciente: String, var numeroUtente: String, var dataNascimento: String, var contacto: String)  {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaPacientes.CAMPO_NOME_PACIENTE, nomePaciente)
            put(TabelaPacientes.CAMPO_NUMERO_UTENTE, numeroUtente)
            put(TabelaPacientes.CAMPO_DATA_NASCIMENTO, dataNascimento)
            put(TabelaPacientes.CAMPO_CONTACTO, contacto)

        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Paciente {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colNomePaciente = cursor.getColumnIndex(TabelaPacientes.CAMPO_NOME_PACIENTE)
            val colNumeroUtente = cursor.getColumnIndex(TabelaPacientes.CAMPO_NUMERO_UTENTE)
            val colDataNascimento = cursor.getColumnIndex(TabelaPacientes.CAMPO_DATA_NASCIMENTO)
            val colContacto = cursor.getColumnIndex(TabelaPacientes.CAMPO_CONTACTO)


            val id = cursor.getLong(colId)
            val nomePaciente = cursor.getString(colNomePaciente)
            val numeroUtente = cursor.getString(colNumeroUtente)
            val dataNascimento = cursor.getString(colDataNascimento)
            val contacto = cursor.getString(colContacto)


            return Paciente(id, nomePaciente, numeroUtente, dataNascimento, contacto)
        }
    }
}