package com.example.covid

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covid.databinding.FragmentListaInfetadosBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ListaInfetadosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaInfetadosFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>{

    private var _binding: FragmentListaInfetadosBinding? = null
    private var adapterInfetados : AdapterInfetados? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_lista_infetados
        // Inflate the layout for this fragment
        _binding = FragmentListaInfetadosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewLivros = view.findViewById<RecyclerView>(R.id.RecyclerViewInfetados)
        adapterInfetados = AdapterInfetados(this)
        recyclerViewLivros.adapter = adapterInfetados
        recyclerViewLivros.layoutManager = LinearLayoutManager(requireContext())

        LoaderManager.getInstance(this)
            .initLoader(ID_LOADER_MANAGER_INFETADOS, null, this)
    }
    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {

            else -> return false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderCovid.ENDERECO_INFETADOS,
            TabelaInfetados.TODAS_COLUNAS,
            null,null,
            TabelaInfetados.CAMPO_DATA_INFECAO
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterInfetados!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterInfetados!!.cursor = null
    }
    companion object {
        const val ID_LOADER_MANAGER_INFETADOS = 0
    }
}