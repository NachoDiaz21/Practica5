package net.iessochoa.ignaciodiazmirete.practica5.ui

import android.graphics.Color
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import net.iessochoa.ignaciodiazmirete.practica5.R
import net.iessochoa.ignaciodiazmirete.practica5.databinding.FragmentTareaBinding
import net.iessochoa.ignaciodiazmirete.practica5.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@Suppress("DEPRECATION")
class TareaFragment : Fragment() {

    val args: TareaFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()
    //será una tarea nueva si no hay argumento
    val esNuevo by lazy { args.tarea==null }


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
        iniciaSwPagado()
        iniciaRgEstado()
        iniciaSbHoras()
        iniciaFabGuardar()

        if (esNuevo)//nueva tarea
        //cambiamos el título de la ventana
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Nueva tarea"
        else
            iniciaTarea(args.tarea!!)

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
            binding.spCategoria.adapter = adapter
            binding.spCategoria.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, v: View?, posicion: Int, id: Long) {
                    val valor=binding.spCategoria.getItemAtPosition(posicion)
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
            binding.spPrioridad.adapter = adapter
            binding.spPrioridad.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
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

    private fun guardaTarea() {
        //recuperamos los datos
        val categoria=binding.spCategoria.selectedItemPosition
        val prioridad=binding.spPrioridad.selectedItemPosition
        val pagado=binding.swPagado.isChecked
        val estado=when (binding.rgEstado.checkedRadioButtonId) {
            R.id.rbAbierta -> 0
            R.id.rbEnCurso -> 1
            else -> 2
        }
        val horas=binding.sbHoras.progress
        val valoracion=binding.rtbValoracion.rating
        val tecnico=binding.etTecnico.text.toString()
        val descripcion=binding.etDescripcion.text.toString()
        //creamos la tarea: si es nueva, generamos un id, en otro caso le  asignamos su id
        val tarea = if(esNuevo)

            Tarea(categoria,prioridad,pagado,estado,horas,valoracion,tecnico,descripcion)
        else

            Tarea(args.tarea!!.id,categoria,prioridad,pagado,estado,horas,valoracion,tecnico,descripcion)
        //guardamos la tarea desde el viewmodel
        viewModel.addTarea(tarea)
        //salimos de editarFragment
        findNavController().popBackStack()
    }

    private fun iniciaFabGuardar(){
        binding.fabGuardar.setOnClickListener{
            if(binding.etTecnico.text.toString().isEmpty() || binding.etDescripcion.text.toString().isEmpty())
                muestraMensajeError()
            else
                guardaTarea()
        }
    }

    private fun muestraMensajeError() {
        Toast.makeText(requireContext(), "Los campos Técnico y/o Descripción no pueden estar vacíos", Toast.LENGTH_SHORT).show()
    }

    /**
     * Carga los valores de la tarea a editar
     */
    private fun iniciaTarea(tarea: Tarea) {
        binding.spCategoria.setSelection(tarea.categoria)
        binding.spPrioridad.setSelection(tarea.prioridad)
        binding.swPagado.isChecked = tarea.pagado
        binding.rgEstado.check(
            when (tarea.estado) {
                0 -> R.id.rbAbierta
                1 -> R.id.rbEnCurso
                else -> R.id.rbCerrada
            }
        )
        binding.sbHoras.progress = tarea.horasTrabajo
        binding.rtbValoracion.rating = tarea.valoracionCliente
        binding.etTecnico.setText(tarea.tecnico)
        binding.etDescripcion.setText(tarea.descripcion)
        //cambiamos el título
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Tarea ${tarea.id}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaSwPagado() {
        binding.swPagado.setOnCheckedChangeListener { _, isChecked ->
            //cambiamos el icono si está marcado o no el switch
            val imagen=if (isChecked) R.drawable.ic_pagado
            else R.drawable.ic_no_pagado
            //asignamos la imagen desde recursos
            binding.ivPagado.setImageResource(imagen)
        }
        //iniciamos a valor false
        binding.swPagado.isChecked=false
        binding.ivPagado.setImageResource(R.drawable.ic_no_pagado)
    }

    private fun iniciaRgEstado() {
        // Asignar un OnClickListener a cada RadioButton
        binding.rbAbierta.setOnClickListener {
            binding.ivEstado.setImageResource(R.drawable.ic_abierto)
        }

        binding.rbEnCurso.setOnClickListener {
            binding.ivEstado.setImageResource(R.drawable.ic_encurso)
        }

        binding.rbCerrada.setOnClickListener {
            binding.ivEstado.setImageResource(R.drawable.ic_cerrado)
        }

        // Iniciamos a abierto por defecto
        binding.rbAbierta.isChecked = true
    }

    private fun iniciaSbHoras() {
        //asignamos el evento
        binding.sbHoras.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progreso: Int, p2: Boolean) {
                //Mostramos el progreso en el textview
                binding.tvHoras.text=getString(R.string.horas_trabajadas,progreso)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        //inicio del progreso
        binding.sbHoras.progress=0
        binding.tvHoras.text=getString(R.string.horas_trabajadas,0)
    }
}