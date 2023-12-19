package net.iessochoa.ignaciodiazmirete.practica5.repository

import android.app.Application
import android.content.Context
import net.iessochoa.ignaciodiazmirete.practica5.ui.ModelTempTareas
import net.iessochoa.ignaciodiazmirete.practica5.ui.Tarea

object Repository {
    //instancia al modelo
    private lateinit var modelTareas:ModelTempTareas
    //el context suele ser necesario para recuperar datos
    private lateinit var application: Application
    //inicio del objeto singleton
    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        //iniciamos el modelo
        ModelTempTareas(application)
        modelTareas=ModelTempTareas
    }
    fun addTarea(tarea: Tarea)= modelTareas.addTarea(tarea)
    fun delTarea(tarea: Tarea)= modelTareas.delTarea(tarea)
    fun getAllTareas()=modelTareas.getAllTareas()
}