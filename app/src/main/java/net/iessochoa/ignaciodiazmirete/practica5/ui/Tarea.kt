package net.iessochoa.ignaciodiazmirete.practica5.ui

data class Tarea(
    var id:Long?=null,//id único
    val categoria:Int,
    val prioridad:Int,
    val pagado:Boolean,
    val estado:Int,
    val horasTrabajo:Int,
    val valoracionCliente:Float,
    val tecnico:String,
    val descripcion:String
)
