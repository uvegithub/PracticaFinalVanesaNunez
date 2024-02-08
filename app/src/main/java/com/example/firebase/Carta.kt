package com.example.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Carta(
    var id : String? = null,
    var nombre: String? = null,
    var precio: String? = null,
    var disponible:String? = null,
    var categoria:String? = null,
    var imagen: String? = null,
    var estado_notificacion:Int? = null,
    var user_notificacion:String? = null,
): Parcelable
