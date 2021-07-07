package com.example.covid

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditaPacienteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditaPacienteFragment : Fragment() {
    private lateinit var  editTextNomePaciente : EditText
    private lateinit var editTextNumeroUtente :EditText
    private lateinit var editTextDataNascimento :EditText
    private lateinit var editTextMorada : EditText
    private lateinit var editTextContacto :EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_edita_paciente
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edita_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextNomePaciente = view.findViewById(R.id.editTextNomePaciente)
        editTextNumeroUtente = view.findViewById(R.id.editTextNumeroUtente)
        editTextDataNascimento = view.findViewById(R.id.editTextDataNascimento)
        editTextMorada = view.findViewById(R.id.editTextMorada)
        editTextContacto = view.findViewById(R.id.editTextContacto)

        editTextNomePaciente.setText(DadosApp.pacienteSelecionado!!.nomePaciente)
        editTextNumeroUtente.setText(DadosApp.pacienteSelecionado!!.numeroUtente)
        editTextDataNascimento.setText(DadosApp.pacienteSelecionado!!.dataNascimento.toString())
        editTextMorada.setText(DadosApp.pacienteSelecionado!!.morada)
        editTextContacto.setText(DadosApp.pacienteSelecionado!!.contacto)
    }
    fun navegaListaPacientes(){
        findNavController().navigate(R.id.action_EditaPacienteFragment_to_ListaPacientesFragment)
    }
    fun guardar(){
        val nomePaciente = editTextNomePaciente.text.toString()
        if (nomePaciente.isEmpty()) {
            editTextNomePaciente.setError("")
            editTextNomePaciente.requestFocus()
            return
        }

        val numeroUtente = editTextNumeroUtente.text.toString()
        if (numeroUtente.isEmpty()) {
            editTextNumeroUtente.setError("")
            editTextNumeroUtente.requestFocus()
            return
        }
        val dataNascimento = editTextDataNascimento.text.toString()
        if (dataNascimento.isEmpty()) {
            editTextDataNascimento.requestFocus()
            return
        }
        val morada = editTextMorada.text.toString()
        if (morada.isEmpty()) {
            editTextMorada.requestFocus()
            return
        }
        val contacto = editTextContacto.text.toString()
        if (contacto.isEmpty()) {
            editTextContacto.setError("")
            editTextContacto.requestFocus()
            return
        }



        val paciente = DadosApp.pacienteSelecionado!!
        paciente.nomePaciente = nomePaciente
        paciente.numeroUtente = numeroUtente
        paciente.dataNascimento = Date(dataNascimento)
        paciente.morada = morada
        paciente.contacto = contacto

        val uriPacientes = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_PACIENTES,
            paciente.id.toString()
        )

        val registos = activity?.contentResolver?.update(
            uriPacientes,
            paciente.toContentValues(),
            null,
            null
        )

        if (registos != 1) {
            Toast.makeText(
                requireContext(),
                "Erro ao alterar paciente",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Paciente guardado com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaPacientes()
    }
    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_edita_paciente -> guardar()
            R.id.action_cancelar_edita_paciente -> navegaListaPacientes()
            else -> return false
        }

        return true
    }
}


