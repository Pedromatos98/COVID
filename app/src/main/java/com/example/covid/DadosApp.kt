package com.example.covid


class DadosApp {
        companion object {
            lateinit var activity: MainActivity
             var listaPacientesFragment: ListaPacientesFragment ?=null
             var novoPacienteFragment: NovoPacienteFragment ?=null
            var pacienteSelecionado : Paciente? = null
        }
}
