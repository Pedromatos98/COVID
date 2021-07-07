package com.example.covid


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.covid.databinding.FragmentNovoPacienteBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*


/**
* A simple [Fragment] subclass as the second destination in the navigation.
*/
class NovoPacienteFragment : Fragment() {

    private var _binding: FragmentNovoPacienteBinding? = null

    private lateinit var editTextNomePaciente: EditText
    private lateinit var editTextNumeroUtente: EditText
    private lateinit var editTextDataNascimento: EditText
    private lateinit var editTextMorada:EditText
    private lateinit var editTextContacto: EditText

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_novo_paciente

        _binding = FragmentNovoPacienteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextNomePaciente = view.findViewById(R.id.editTextNomePaciente)
        editTextNumeroUtente = view.findViewById(R.id.editTextNumeroUtente)
        editTextDataNascimento = view.findViewById(R.id.editTextDataNascimento)
        editTextMorada = view.findViewById(R.id.editTextMorada)
        editTextContacto = view.findViewById(R.id.editTextContacto)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navegaListaPacientes() {
        findNavController().navigate(R.id.action_novoPacienteFragment_to_listaPacientesFragment)
    }

    fun guardar() {
        val nomePaciente = editTextNomePaciente.text.toString()
        if (nomePaciente.isEmpty()) {
            editTextNomePaciente.setError("Preencha o nome do paciente")
            return
        }

        val numeroUtente = editTextNumeroUtente.text.toString()
        if (numeroUtente.isEmpty()) {
            editTextNumeroUtente.setError("Preencha o número de utente")
            return
        }
        val dataNascimento = editTextDataNascimento.text.toString()
        if (dataNascimento.isEmpty()) {
            return
        }
        val morada = editTextMorada.text.toString()
        if (dataNascimento.isEmpty()) {
            return
        }
        val contacto = editTextContacto.text.toString()
        if (contacto.isEmpty()) {
            editTextContacto.setError("Preencha o contacto")
            return
        }

        val paciente = Paciente(
            nomePaciente = nomePaciente,
            numeroUtente = numeroUtente,
            dataNascimento = Date(dataNascimento) ,
            morada = morada,
            contacto = contacto
        )

        val uri = activity?.contentResolver?.insert(
            ContentProviderCovid.ENDERECO_PACIENTES,
            paciente.toContentValues()
        )

        if (uri == null) {
            Snackbar.make(
                editTextContacto,
                "Erro ao inserir Paciente",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        navegaListaPacientes()
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_novo_paciente -> guardar()
            R.id.action_cancelar_novo_paciente ->navegaListaPacientes()
            else -> return false
        }

        return true
    }



}




