package com.example.covid

import android.database.Cursor
import android.net.Uri
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
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditaInfetadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditaInfetadoFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>{

    private lateinit var editTextDataInfecao : EditText
    private lateinit var editTextSintomas : EditText
    private lateinit var spinnerPacientes: Spinner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_edita_infetado

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edita_infetado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDataInfecao = view.findViewById(R.id.editTextDataInfecao)
        editTextSintomas = view.findViewById(R.id.editTextSintomas)
        spinnerPacientes = view.findViewById(R.id.spinnerNomePaciente)

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_PACIENTES, null, this)

        editTextDataInfecao.setText(DadosApp.infetadoSelecionado!!.dataInfecao.toString())
        editTextSintomas.setText(DadosApp.infetadoSelecionado!!.sintomas)
    }

    fun navegaListaInfetados() {
        findNavController().navigate(R.id.action_editaInfetadoFragment_to_listaInfetadosFragment)
    }

    fun guardar() {
        val dataInfecao = editTextDataInfecao.text.toString()
        if (dataInfecao.isEmpty()) {
            editTextDataInfecao.setError("Preencha a data de infeção")
            editTextDataInfecao.requestFocus()
            return
        }

        val sintomas = editTextSintomas.text.toString()
        if (sintomas.isEmpty()) {
            editTextSintomas.setError("Preencha os Sintomas")
            editTextSintomas.requestFocus()
            return
        }

        val idPaciente = spinnerPacientes.selectedItemId

        val infetado = DadosApp.infetadoSelecionado!!
        infetado.dataInfecao = Date(dataInfecao)
        infetado.sintomas = sintomas
        infetado.idPaciente = idPaciente

        val uriInfetado = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_INFETADOS,
            infetado.id.toString()
        )

        val registos = activity?.contentResolver?.update(
            uriInfetado,
            infetado.toContentValues(),
            null,
            null
        )

        if (registos != 1) {
            Toast.makeText(
                requireContext(),"Erro ao alterar infetado",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Infetado guardado com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaInfetados()
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_edita_infetado -> guardar()
            R.id.action_cancelar_edita_infetado -> navegaListaInfetados()
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
        atualizaSpinnerPacientes(data)
        atualizaPacienteSelecionado()
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
        atualizaSpinnerPacientes(null)
    }

    private fun atualizaSpinnerPacientes(data: Cursor?) {
        spinnerPacientes.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(TabelaPacientes.CAMPO_NOME_PACIENTE),
            intArrayOf(android.R.id.text1),
            0
        )
    }

    private fun atualizaPacienteSelecionado() {
        val idNomePaciente = DadosApp.infetadoSelecionado!!.idPaciente

        val ultimoPaciente = spinnerPacientes.count - 1
        for (i in 0..ultimoPaciente) {
            if (idNomePaciente == spinnerPacientes.getItemIdAtPosition(i)) {
                spinnerPacientes.setSelection(i)
                return
            }
        }
    }

    companion object {
        const val ID_LOADER_MANAGER_PACIENTES = 0
    }
}