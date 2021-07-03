package com.example.covid

import androidx.fragment.app.Fragment


class DadosApp {
        companion object {
            lateinit var activity: MainActivity
            lateinit var fragment: Fragment
            var pacienteSelecionado : Paciente? = null
            var infetadoSelecionado : Infetado? = null
        }
}
