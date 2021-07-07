package com.example.covid

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [EliminaPacienteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EliminaPacienteFragment : Fragment() {
    private lateinit var textViewNomePaciente : TextView
    private lateinit var textViewNumeroUtente : TextView
    private lateinit var textViewDataNascimento : TextView
    private lateinit var textViewMorada : TextView
    private lateinit var textViewContacto : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_elimina_paciente
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elimina_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNomePaciente = view.findViewById(R.id.textViewNomePaciente)
        textViewNumeroUtente = view.findViewById(R.id.textViewNumeroUtente)
        textViewDataNascimento = view.findViewById(R.id.textViewIdade)
        textViewMorada = view.findViewById(R.id.textViewMorada)
        textViewContacto = view.findViewById(R.id.textViewContacto)

        val paciente = DadosApp.pacienteSelecionado!!
        textViewNomePaciente.setText(paciente.nomePaciente)
        textViewNumeroUtente.setText(paciente.numeroUtente)
        textViewDataNascimento.setText(paciente.dataNascimento.toString())
        textViewMorada.setText(paciente.morada)
        textViewContacto.setText(paciente.contacto)

    }

    fun navegaListaPacientes(){
        findNavController().navigate(R.id.action_eliminaPacienteFragment_to_ListaPacientesFragment)
    }
    fun elimina() {
        val uriLivro = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_PACIENTES,
            DadosApp.pacienteSelecionado!!.id.toString()
        )

        val registos = activity?.contentResolver?.delete(
            uriLivro,
            null,
            null
        )

        if (registos != 1) {
            Toast.makeText(
                requireContext(),"Erro ao eliminar paciente",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Paciente eliminado com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaPacientes()
    }
    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_confirma_eliminar_paciente -> elimina()
            R.id.action_cancelar_eliminar_paciente -> navegaListaPacientes()
            else -> return false
        }

        return true
    }


}