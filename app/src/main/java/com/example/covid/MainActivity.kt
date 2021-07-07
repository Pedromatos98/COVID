package com.example.covid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.covid.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var menu: Menu

    var menuAtual = R.menu.menu_principal
    set(value){
        field = value
        invalidateOptionsMenu()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        DadosApp.activity = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(menuAtual, menu)
        this.menu = menu

        if (menuAtual == R.menu.menu_lista_pacientes) {
            atualizaMenuListaPacientes(false)
        }
        if (menuAtual == R.menu.menu_lista_infetados) {
            atualizaMenuListaInfetados(false)
        }
        if (menuAtual == R.menu.menu_lista_vacinados) {
            atualizaMenuListaVacinados(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val opcaoProcessada = when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "COVID v. 1.0", Toast.LENGTH_LONG).show()
                true
            }
            else -> when (menuAtual) {
                R.menu.menu_lista_pacientes -> (DadosApp.fragment as ListaPacientesFragment).processaOpcaoMenu(item)
                R.menu.menu_novo_paciente -> (DadosApp.fragment as NovoPacienteFragment).processaOpcaoMenu(item)
                R.menu.menu_edita_paciente ->(DadosApp.fragment as EditaPacienteFragment).processaOpcaoMenu(item)
                R.menu.menu_elimina_paciente ->(DadosApp.fragment as EliminaPacienteFragment).processaOpcaoMenu(item)
                R.menu.menu_lista_infetados ->(DadosApp.fragment as ListaInfetadosFragment).processaOpcaoMenu(item)
                R.menu.menu_novo_infetado ->(DadosApp.fragment as NovoInfetadoFragment).processaOpcaoMenu(item)
                R.menu.menu_edita_infetado ->(DadosApp.fragment as EditaInfetadoFragment).processaOpcaoMenu(item)
                R.menu.menu_elimina_infetado ->(DadosApp.fragment as EliminaInfetadoFragment).processaOpcaoMenu(item)
                R.menu.menu_lista_vacinados ->(DadosApp.fragment as ListaVacinadosFragment).processaOpcaoMenu(item)
                R.menu.menu_novo_vacinado ->(DadosApp.fragment as NovoVacinadoFragment).processaOpcaoMenu(item)
                R.menu.menu_elimina_vacinado ->(DadosApp.fragment as EliminaVacinadoFragment).processaOpcaoMenu(item)
                else -> false
            }
        }
        return if(opcaoProcessada) true else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun atualizaMenuListaPacientes(mostraBotoesAlterarEliminar : Boolean) {
        menu.findItem(R.id.action_alterar_paciente).setVisible(mostraBotoesAlterarEliminar)
        menu.findItem(R.id.action_eliminar_paciente).setVisible(mostraBotoesAlterarEliminar)
    }
    fun atualizaMenuListaInfetados(mostraBotoesAlterarEliminar : Boolean) {
        menu.findItem(R.id.action_alterar_infetado).setVisible(mostraBotoesAlterarEliminar)
        menu.findItem(R.id.action_eliminar_infetado).setVisible(mostraBotoesAlterarEliminar)
    }
    fun atualizaMenuListaVacinados(mostraBotoesAlterarEliminar : Boolean) {
        menu.findItem(R.id.action_alterar_vacinado).setVisible(mostraBotoesAlterarEliminar)
        menu.findItem(R.id.action_eliminar_vacinado).setVisible(mostraBotoesAlterarEliminar)
    }
}