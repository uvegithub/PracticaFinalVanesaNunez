package com.example.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento(
    var id : String? = null,
    var nombre: String? = null,
    var precio: Float? = null,
    var aforo_max:Int? = null,
    var aforo_ocupado:Int? = null,
    var estado_notificacion:Int? = null,
    var user_notificacion:String? = null,
): Parcelable
