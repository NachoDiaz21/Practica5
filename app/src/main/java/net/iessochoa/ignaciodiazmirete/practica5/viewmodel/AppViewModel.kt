package net.iessochoa.ignaciodiazmirete.practica5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import net.iessochoa.ignaciodiazmirete.practica5.repository.Repository
import net.iessochoa.ignaciodiazmirete.practica5.ui.Tarea
class AppViewModel (application: Application) : AndroidViewModel(application) {
    //repositorio
    private val repositorio: Repository
    //liveData de lista de tareas
    val tareasLiveData : LiveData<List<Tarea>>
    //creamos el LiveData de tipo Booleano. Representa nuestro filtro
    private val soloSinPagarLiveData= MutableLiveData<Boolean>(false)
    //inicio ViewModel
    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        //tareasLiveData =repositorio.getAllTareas()
        tareasLiveData=soloSinPagarLiveData.switchMap {soloSinPagar-> Repository.getTareasFiltroSinPagar(soloSinPagar)}
    }
    fun addTarea(tarea: Tarea) = repositorio.addTarea(tarea)
    fun delTarea(tarea: Tarea) = repositorio.delTarea(tarea)
    /**
     * activa el LiveData del filtro
     */
    fun setSoloSinPagar(soloSinPagar:Boolean){soloSinPagarLiveData.value=soloSinPagar}
}