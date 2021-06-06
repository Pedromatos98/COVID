package com.example.covid

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
    private fun getCovidOpenHelper() = BdCovidOpenHelper(getAppContext())

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

    



    @Before
    fun apagaBaseDados() {
        getAppContext().deleteDatabase(BdCovidOpenHelper.NOME_BASE_DADOS)
    }
    @Test
    fun consegueAbrirBaseDados() {
        val db = getCovidOpenHelper().readableDatabase
        assert(db.isOpen)
        db.close()
    }
}