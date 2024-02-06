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
    lateinit var boton_registro: Button
    lateinit var intento_registro: Intent

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
        boton_registro=findViewById(R.id.button_registrarse)

        boton_validar.setOnClickListener {
            validacion(user_layout, contrasena_layout, user_edit, contrasena_edit)
        }

        intento = Intent(this, MainActivity::class.java)
        intento_registro = Intent(this, MainActivity::class.java)

        boton_registro.setOnClickListener {
            startActivity(intento_registro)
        }
    }

    fun validacion(usuario_layaout:TextInputLayout, contrasenalayout: TextInputLayout, usuario_edit:TextInputEditText, contrasenaedit:TextInputEditText){
        //admin: login:admin  contra:admin   usuario  login:user   contrasena: user
        if(usuario_edit.text.toString()=="admin" && contrasenaedit.text.toString()=="admin"){
            startActivity(intento)
        }else if(usuario_edit.text.toString()=="user" && contrasenaedit.text.toString()=="user"){
            startActivity(intento)
        }else{
            if(usuario_edit.text.toString()==""){
                usuario_layaout.setError("Debe introducir el nombre de usuario")
            }else if(contrasenaedit.text.toString()==""){
                contrasenalayout.setError("Debe introducir la contrasena")
            }else if(usuario_edit.text.toString()=="admin" || usuario_edit.text.toString()=="user"){
                usuario_layaout.setError("Nombre de usuario incorrecto")
            }else if(contrasenaedit.text.toString()=="admin"|| contrasenaedit.text.toString()=="user"){
                contrasenalayout.setError("Contrasena incorrecta")
            }
        }
    }

}