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
 * Use the [EliminaVacinadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EliminaVacinadoFragment : Fragment() {
    private lateinit var textViewNomeVacinado: TextView
    private lateinit var textViewDataAdmnistracao: TextView
    private lateinit var textViewNumeroAdmnistracoes: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_elimina_vacinado

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elimina_vacinado, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNomeVacinado = view.findViewById(R.id.textViewNomeVacinado)
        textViewDataAdmnistracao= view.findViewById(R.id.textViewDataAdmnistracao)
        textViewNumeroAdmnistracoes = view.findViewById(R.id.textViewNumAdmn)

        val vacinado = DadosApp.vacinadoSelecionado!!
        textViewNomeVacinado.setText(vacinado.nomePaciente)
        textViewDataAdmnistracao.setText(vacinado.dataAdmnistracao.toString())
        textViewNumeroAdmnistracoes.setText(vacinado.numeroAdmnistracoes)
    }

    fun navegaListaVacinados() {
        findNavController().navigate(R.id.action_eliminaVacinadoFragment_to_listaVacinadosFragment)
    }

    fun elimina() {
        val uriLivro = Uri.withAppendedPath(
            ContentProviderCovid.ENDERECO_VACINADOS,
            DadosApp.vacinadoSelecionado!!.id.toString()
        )

        val registos = activity?.contentResolver?.delete(
            uriLivro,
            null,
            null
        )

        if (registos != 1) {
            Toast.makeText(
                requireContext(),
                "Erro ao eliminar Vacinado",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Vacinado eliminado com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaVacinados()
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_confirma_eliminar_vacinado -> elimina()
            R.id.action_cancelar_eliminar_vacinado -> navegaListaVacinados()
            else -> return false
        }

        return true
    }
}
