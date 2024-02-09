package com.example.firebase

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem

class Ver_cartas : AppCompatActivity() {

    private lateinit var rol_usuario: String

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_cartas)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if(rol_usuario == "admin"){
            menuInflater.inflate(R.menu.menu_admin, menu)
        }else{
            menuInflater.inflate(R.menu.menu_user, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.accion_ver_cartas){
            val intent = Intent(this, Ver_cartas::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.accion_crear_cartas){
            val intent = Intent(this, Crear_carta::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.accion_editar_cartas){
            val intent = Intent(this, Editar_carta::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}