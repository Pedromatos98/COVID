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
 * Use the [EliminaInfetadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EliminaInfetadoFragment : Fragment() {
    private lateinit var textViewNomePacienteInfetado: TextView
    private lateinit var textViewDataInfecao: TextView
    private lateinit var textViewSintomas: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_elimina_infetado

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elimina_infetado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNomePacienteInfetado = view.findViewById(R.id.textViewNomePacienteInfetado)
        textViewDataInfecao = view.findViewById(R.id.textViewDataInfecao)
        textViewSintomas = view.findViewById(R.id.textViewSintomas)

        val infetado = DadosApp.infetadoSelecionado!!
        textViewNomePacienteInfetado.setText(infetado.nomePaciente)
        textViewDataInfecao.setText(infetado.dataInfecao.toString())
        textViewSintomas.setText(infetado.sintomas)

    }

    fun navegaListaInfetados() {
        findNavController().navigate(R.id.action_eliminaInfetadoFragment_to_listaInfetadosFragment)
    }

    fun elimina() {
        val uriLivro = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_INFETADOS,
            DadosApp.infetadoSelecionado!!.id.toString()
        )

        val registos = activity?.contentResolver?.delete(
            uriLivro,
            null,
            null
        )

        if (registos != 1) {
            Toast.makeText(
                requireContext(),
                "Erro ao eliminar infetado",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Infetado inserido com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaInfetados()
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_confirma_eliminar_infetado -> elimina()
            R.id.action_cancelar_eliminar_infetado -> navegaListaInfetados()
            else -> return false
        }

        return true
    }
}