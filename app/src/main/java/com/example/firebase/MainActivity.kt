package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var user_layout: TextInputLayout
    lateinit var contrasena_layout: TextInputLayout
    lateinit var boton_validar: Button
    lateinit var intento: Intent
    lateinit var user_edit: TextInputEditText
    lateinit var contrasena_edit: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val ref=FirebaseDatabase.getInstance().getReference()
//
//        ref.child("Hola").setValue("loooooococococococ")

        user_layout=findViewById(R.id.textinputlayoutUsuario)
        contrasena_layout=findViewById(R.id.textinputlayoutContrasena)
        user_edit=findViewById(R.id.textinputedittextusuario)
        contrasena_edit=findViewById(R.id.textinputedittextcontrasena)
        boton_validar=findViewById(R.id.button)

        boton_validar.setOnClickListener {
            validacion()
        }

        intento = Intent(this, MainActivity::class.java)
    }

    fun validacion(){
        //admin: login:admin  contra:admin   usuario  login:user   contrasena: user
    }
}