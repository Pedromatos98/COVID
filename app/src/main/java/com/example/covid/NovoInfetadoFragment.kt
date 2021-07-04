package com.example.covid

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.example.covid.databinding.FragmentNovoInfetadoBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [NovoInfetadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NovoInfetadoFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentNovoInfetadoBinding? = null

    private lateinit var editTextDataInfecao: EditText
    private lateinit var editTextSintomas: EditText
    private lateinit var spinnerNomePaciente: Spinner

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_novo_infetado

        _binding = FragmentNovoInfetadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDataInfecao = view.findViewById(R.id.editTextDataInfecao)
        editTextSintomas = view.findViewById(R.id.editTextSintomas)
        spinnerNomePaciente = view.findViewById(R.id.spinnerNomePaciente)

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_PACIENTES, null, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_PACIENTES,
            TabelaPacientes.TODAS_COLUNAS,
            null, null,
            TabelaPacientes.CAMPO_NOME_PACIENTE
        )
    }
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        atualizaSpinnerPacientes(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        atualizaSpinnerPacientes(null)
    }

    private fun atualizaSpinnerPacientes(data: Cursor?) {
        spinnerNomePaciente.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(TabelaPacientes.CAMPO_NOME_PACIENTE),
            intArrayOf(android.R.id.text1),
            0
        )
    }
    companion object {
        const val ID_LOADER_MANAGER_PACIENTES = 0
    }





}