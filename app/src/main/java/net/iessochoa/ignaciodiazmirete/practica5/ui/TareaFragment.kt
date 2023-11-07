package net.iessochoa.ignaciodiazmirete.practica5.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import net.iessochoa.ignaciodiazmirete.practica5.R
import net.iessochoa.ignaciodiazmirete.practica5.databinding.FragmentTareaBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@Suppress("DEPRECATION")
class TareaFragment : Fragment() {

    private var _binding: FragmentTareaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTareaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
        iniciaSpCategoria()
        iniciaSpPrioridad()
        binding.root.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun iniciaSpCategoria() {
        ArrayAdapter.createFromResource(
            //contexto suele ser la Activity
            requireContext(),
            //array de strings
            R.array.categoria,
            //layout para mostrar el elemento seleccionado
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnCategoria.adapter = adapter
            binding.spnCategoria.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, v: View?, posicion: Int, id: Long) {
                    val valor=binding.spnCategoria.getItemAtPosition(posicion)
                    val mensaje=getString(R.string.mensaje_categoria,valor)
                    Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
    private fun iniciaSpPrioridad() {
        ArrayAdapter.createFromResource(
            //contexto suele ser la Activity
            requireContext(),
            //array de strings
            R.array.prioridad,
            //layout para mostrar el elemento seleccionado
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Layout para mostrar la apariencia de la lista
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // asignamos el adaptador al spinner
            binding.spnPrioridad.adapter = adapter
            binding.spnPrioridad.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, v: View?, posicion: Int, id: Long) {
                    //el array son 3 elementos y "alta" ocupa la tercera posición
                    if(posicion==2){
                        binding.clytTarea.setBackgroundColor(requireContext().getColor(R.color.prioridad_alta))
                    }else{//si no es prioridad alta quitamos el color
                        binding.clytTarea.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.clytTarea.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}