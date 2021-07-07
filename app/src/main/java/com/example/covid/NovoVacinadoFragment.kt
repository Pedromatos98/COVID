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
import com.example.covid.databinding.FragmentNovoVacinadoBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.math.log


/**
 * A simple [Fragment] subclass.
 * Use the [NovoVacinadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NovoVacinadoFragment : Fragment(),LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentNovoVacinadoBinding? = null

    private lateinit var editTextDataAdmnistracao: EditText
    private lateinit var editTextNumeroAdmnistracoes: EditText
    private lateinit var spinnerPacientes: Spinner

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_novo_vacinado

        _binding = FragmentNovoVacinadoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDataAdmnistracao = view.findViewById(R.id.editTextDataAdmnistracao)
        editTextNumeroAdmnistracoes = view.findViewById(R.id.editTextNumeroAdmnistracoes)
        spinnerPacientes = view.findViewById(R.id.spinnerNomePaciente)

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_PACIENTES, null, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navegaListaVacinados() {
        findNavController().navigate(R.id.action_novoVacinadoFragment_to_listaVacinadosFragment)
    }

    fun guardar() {
        val dataAdmnistracao = editTextDataAdmnistracao.text.toString()
        if (dataAdmnistracao.isEmpty()) {
            editTextDataAdmnistracao.setError("Preencha a data de admnistração")
            editTextDataAdmnistracao.requestFocus()
            return
        }

        val nAdmnistracoes = editTextNumeroAdmnistracoes.text.toString()
        if (nAdmnistracoes.isEmpty()) {
            editTextNumeroAdmnistracoes.setError("Preencha o numero de admnistracoes")
            editTextNumeroAdmnistracoes.requestFocus()
            return
        }

        val idPaciente = spinnerPacientes.selectedItemId

        val vacinado = Vacinado(dataAdmnistracao = Date(dataAdmnistracao) ,numeroAdmnistracoes = nAdmnistracoes,   idPaciente = idPaciente)

        val uri = activity?.contentResolver?.insert(
            ContentProviderCovid.ENDERECO_VACINADOS,
            vacinado.toContentValues()
        )

        if (uri == null) {
            Snackbar.make(
                editTextDataAdmnistracao, "Erro ao inserir Vacinado",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Vacinado inserido com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaVacinados()
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_novo_vacinado -> guardar()
            R.id.action_cancelar_novo_vacinado -> navegaListaVacinados()
            else -> return false
        }

        return true
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param id The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_PACIENTES,
            TabelaPacientes.TODAS_COLUNAS,
            null, null,
            TabelaPacientes.CAMPO_NOME_PACIENTE
        )
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is *not* allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See [ FragmentManager.openTransaction()][androidx.fragment.app.FragmentManager.beginTransaction] for further discussion on this.
     *
     *
     * This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     *
     *  *
     *
     *The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a [android.database.Cursor]
     * and you place it in a [android.widget.CursorAdapter], use
     * the [android.widget.CursorAdapter.CursorAdapter] constructor *without* passing
     * in either [android.widget.CursorAdapter.FLAG_AUTO_REQUERY]
     * or [android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER]
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     *  *  The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a [android.database.Cursor] from a [android.content.CursorLoader],
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * [android.widget.CursorAdapter], you should use the
     * [android.widget.CursorAdapter.swapCursor]
     * method so that the old Cursor is not closed.
     *
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        atualizaSpinnerCategorias(data)
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param loader The Loader that is being reset.
     */
    override fun onLoaderReset(loader: Loader<Cursor>) {
        atualizaSpinnerCategorias(null)
    }

    private fun atualizaSpinnerCategorias(data: Cursor?) {
        spinnerPacientes.adapter = SimpleCursorAdapter(
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

