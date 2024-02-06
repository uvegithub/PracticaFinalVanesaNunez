package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Registro_usuarios : AppCompatActivity() {

    lateinit var user_layout: TextInputLayout
    lateinit var contrasena_layout: TextInputLayout
    lateinit var boton_ingresar: Button
    lateinit var intento: Intent
    lateinit var user_edit: TextInputEditText
    lateinit var contrasena_edit: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuarios)

        user_layout=findViewById(R.id.textinputlayoutUsuario)
        contrasena_layout=findViewById(R.id.textinputlayoutContrasena)
        user_edit=findViewById(R.id.textinputedittextusuario)
        contrasena_edit=findViewById(R.id.textinputedittextcontrasena)
        boton_ingresar=findViewById(R.id.button)
    }
}