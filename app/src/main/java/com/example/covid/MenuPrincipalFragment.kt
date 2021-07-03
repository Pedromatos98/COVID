package com.example.covid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPrincipalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPrincipalFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_principal
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_menu_principal, container, false)

        val botaoPacientes = v.findViewById<Button>(R.id.buttonPacientes)
        botaoPacientes.setOnClickListener {
            findNavController().navigate(R.id.action_menuPrincipalFragment_to_ListaPacientesFragment)

        }

        return v
    }

}