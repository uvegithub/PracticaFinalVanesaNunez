package com.example.firebase

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Job

class VerEventos : AppCompatActivity() {
    private lateinit var rol_usuario: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var recycler: RecyclerView
    private  lateinit var lista:MutableList<Evento>
    private lateinit var adaptador: EventoAdaptador
    private lateinit var db_ref: DatabaseReference

//    private lateinit var storage_ref: StorageReference

//    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_eventos)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        lista = mutableListOf()
        db_ref = FirebaseDatabase.getInstance().getReference()

        db_ref.child("tienda")
            .child("eventos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach { hijo: DataSnapshot?
                        ->
                        val pojo_evento = hijo?.getValue(Evento::class.java)
                        lista.add(pojo_evento!!)
                    }
                    recycler.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        adaptador = EventoAdaptador(lista)
        recycler = findViewById(R.id.lista_eventos)
        recycler.adapter = adaptador
        recycler.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recycler.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if(rol_usuario == "administrador"){
            menuInflater.inflate(R.menu.menu_admin, menu)
        }else{
            menuInflater.inflate(R.menu.menu_user, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accion_ver_cartas -> {
                val intent = Intent(this, Ver_cartas::class.java)
                startActivity(intent)
            }
            R.id.accion_crear_cartas -> {
                val intent2 = Intent(this, Crear_carta::class.java)
                startActivity(intent2)
            }
//            R.id.accion_editar_cartas -> {
//                val intent3 = Intent(this, Editar_carta::class.java)
//                startActivity(intent3)
//            }
            R.id.accion_aceptar_compra -> {
                val intent3 = Intent(this, Mi_cesta::class.java)
                startActivity(intent3)
            }
            R.id.accion_crear_evento -> {
                val intent4 = Intent(this, CrearEvento::class.java)
                startActivity(intent4)
            }
            R.id.accion_ver_eventos -> {
                val intent5 = Intent(this, VerEventos::class.java)
                startActivity(intent5)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}