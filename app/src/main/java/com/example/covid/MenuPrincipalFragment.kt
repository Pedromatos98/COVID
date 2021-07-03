package com.example.covid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.covid.databinding.FragmentMenuPrincipalBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPrincipalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPrincipalFragment : Fragment() {
    private var _binding: FragmentMenuPrincipalBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_principal
        // Inflate the layout for this fragment
        _binding = FragmentMenuPrincipalBinding.inflate(inflater, container, false)



        val botaoPacientes = binding.buttonPacientes
        botaoPacientes.setOnClickListener {
            findNavController().navigate(R.id.action_menuPrincipalFragment_to_ListaPacientesFragment)
        }
        val botaoInfetados = binding.buttonInfetados
        botaoInfetados.setOnClickListener {
            findNavController().navigate(R.id.action_menuPrincipalFragment_to_listaInfetadosFragment)
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

