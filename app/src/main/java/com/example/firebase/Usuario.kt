package com.example.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    var id : String? = null,
    var login: String? = null,
    var password: String? = null,
    var tipo:String? = null,
): Parcelable
