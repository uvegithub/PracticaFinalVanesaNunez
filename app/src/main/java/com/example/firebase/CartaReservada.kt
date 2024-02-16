package com.example.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartaReservada(
    var id_reserva_carta : String? = null,
    var id_carta: String? = null,
    var id_usuario:String? = null,
    var estado:String? = null,
    var imagen: String? = null,
    var estado_notificacion:Int? = null,
    var user_notificacion:String? = null,
): Parcelable
