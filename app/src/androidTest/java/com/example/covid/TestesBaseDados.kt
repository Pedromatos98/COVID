package com.example.covid

import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestesBaseDados {
    private fun getAppContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun getBdCovidOpenHelper() = BdCovidOpenHelper(getAppContext())

    private fun inserePaciente(tabela: TabelaPacientes, paciente: Paciente): Long {
        val id = tabela.insert(paciente.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun insereInfetado(tabela: TabelaInfetados, infetado: Infetado): Long {
        val id = tabela.insert(infetado.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun insereVacinado(tabela: TabelaVacinados, vacinado: Vacinado): Long {
        val id = tabela.insert(vacinado.toContentValues())
        assertNotEquals(-1, id)

        return id
    }

    private fun getPacienteBaseDados(tabela: TabelaPacientes, id: Long): Paciente {
        val cursor = tabela.query(
            TabelaPacientes.TODAS_COLUNAS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return Paciente.fromCursor(cursor)
    }

    private fun getInfetadoBaseDados(tabela: TabelaInfetados, id: Long): Infetado {
        val cursor = tabela.query(
            TabelaInfetados.TODAS_COLUNAS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return Infetado.fromCursor(cursor)
    }

    private fun getVacinadoBaseDados(tabela: TabelaVacinados, id: Long): Vacinado {
        val cursor = tabela.query(
            TabelaVacinados.TODAS_COLUNAS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        assertNotNull(cursor)
        assert(cursor!!.moveToNext())

        return Vacinado.fromCursor(cursor)
    }


    @Before
    fun apagaBaseDados() {
        getAppContext().deleteDatabase(BdCovidOpenHelper.NOME_BASE_DADOS)
    }

    @Test
    fun consegueAbrirBaseDados() {
        val db = getBdCovidOpenHelper().readableDatabase
        assert(db.isOpen)
        db.close()
    }


    @Test
    fun consegueInserirPacientes() {
        val db = getBdCovidOpenHelper().writableDatabase
        val tabelaPacientes = TabelaPacientes(db)

        val paciente = Paciente(nomePaciente = "António Ramos", numeroUtente = "123456789",dataNascimento = "01/07/1990",contacto = "961234567")
        paciente.id = inserePaciente(tabelaPacientes, paciente)

        assertEquals(paciente, getPacienteBaseDados(tabelaPacientes, paciente.id))

        db.close()
    }
    fun consegueAlterarPacientes() {
        val db = getBdCovidOpenHelper().writableDatabase
        val tabelaPacientes = TabelaPacientes(db)

        val paciente = Paciente(nomePaciente = "?", numeroUtente = "?",dataNascimento = "?",contacto = "?")
        paciente.id = inserePaciente(tabelaPacientes, paciente)

        paciente.nomePaciente = "José Costa"
        paciente.numeroUtente = "987654321"
        paciente.dataNascimento = "10-10-2000"
        paciente.contacto="969876543"

        val registosAlterados = tabelaPacientes.update(
            paciente.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(paciente.id.toString())
        )

        assertEquals(1, registosAlterados)

        assertEquals(paciente, getPacienteBaseDados(tabelaPacientes, paciente.id))

        db.close()
    }
}