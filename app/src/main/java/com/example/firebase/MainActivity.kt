package com.example.firebase

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import androidx.core.content.edit
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

    private lateinit var sharedPreferences: SharedPreferences

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

        intento = Intent(this, Ver_cartas::class.java)
        intento_registro = Intent(this, Registro_usuarios::class.java)

        boton_registro.setOnClickListener {
            startActivity(intento_registro)
        }
    }

    fun validacion(usuario_layaout:TextInputLayout, contrasenalayout: TextInputLayout, usuario_edit:TextInputEditText, contrasenaedit:TextInputEditText){
        //admin: login:admin  contra:admin   usuario  login:user   contrasena: user

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        Log.v("log","login_"+user_edit.text.toString().trim())
        var usuario_registrado = sharedPreferences.getString("login_"+user_edit.text.toString().trim(),"")
        var contrasena_registrado = sharedPreferences.getString("password_"+user_edit.text.toString().trim(),"")
        Log.d("USER:", usuario_registrado.toString())
        Log.d("PASSWORD:", contrasena_registrado.toString())

        if(usuario_edit.text.toString()=="admin" && contrasenaedit.text.toString()=="admin"){
            startActivity(intento)
            sharedPreferences.edit {
                putString("usuario", "administrador")
            }
        }else if(usuario_edit.text.toString()=="user" && contrasenaedit.text.toString()=="user"){
            startActivity(intento)
            sharedPreferences.edit {
                putString("usuario","cliente")
            }
        }else {
            Log.v("Contrasenia",contrasenaedit.text.toString())
            Log.v("log",(usuario_edit.text.toString()))

            if(usuario_edit.text.toString()==""){
                usuario_layaout.setError("Debe introducir el nombre de usuario")
            }else if(contrasenaedit.text.toString()==""){
                contrasenalayout.setError("Debe introducir la contrasena")
            }

            else if(usuario_edit.text.toString()!=usuario_registrado){
                usuario_layaout.setError("Nombre de usuario incorrecto")
            }
            else if(contrasenaedit.text.toString()!=contrasena_registrado){
                contrasenalayout.setError("Contrasena incorrecta")
            }
            else if(usuario_edit.text.toString()==usuario_registrado && contrasenaedit.text.toString()==contrasena_registrado) {
                startActivity(intento)
                sharedPreferences.edit {
                putString("usuario", "cliente")
                }
            }
        }
    }

}