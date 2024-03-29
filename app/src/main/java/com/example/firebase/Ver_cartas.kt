package com.example.firebase

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Ver_cartas : AppCompatActivity(){

    private lateinit var rol_usuario: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var imagen_cesta:ImageView

    private lateinit var recycler: RecyclerView
    private  lateinit var lista:MutableList<Carta>
    private lateinit var adaptador: CartaAdaptador
    private lateinit var db_ref: DatabaseReference

    private lateinit var spinner: Spinner

    private lateinit var busqueda:EditText
    private lateinit var boton_busqueda:Button

    private lateinit var storage_ref: StorageReference

    private var url_carta: Uri? = null

//    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_cartas)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        imagen_cesta=findViewById(R.id.cesta)

        if(rol_usuario=="cliente"){
            imagen_cesta.setVisibility(View.VISIBLE)
        }else{
            imagen_cesta.setVisibility(View.INVISIBLE)
        }

        imagen_cesta.setOnClickListener {
            val activity = Intent(applicationContext, Mi_cesta::class.java)
            startActivity(activity)
        }

        spinner= findViewById(R.id.filtro)

        val items = resources.getStringArray(R.array.spinner_items)

        lista = mutableListOf()
        db_ref = FirebaseDatabase.getInstance().getReference()

        db_ref.child("tienda")
            .child("cartas")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{hijo: DataSnapshot?
                        ->
                        val pojo_carta = hijo?.getValue(Carta::class.java)
                        lista.add(pojo_carta!!)
                    }
                    recycler.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }

            })

        adaptador = CartaAdaptador(lista)
        recycler = findViewById(R.id.lista_cartas)
        recycler.adapter = adaptador
        recycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recycler.setHasFixedSize(true)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
//                val item_seleccionado = items[position]
//                lista.filter { it.categoria==item_seleccionado }
//                lista.retainAll { it.categoria==item_seleccionado }

                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        busqueda=findViewById(R.id.textinputedittextNombre)
        boton_busqueda=findViewById(R.id.button)

        boton_busqueda.setOnClickListener {
            if(busqueda.toString()!=""){
                Log.v("lista", lista.toString())
                lista.retainAll{
                    Log.v("tag",busqueda.toString())
                    Log.v("tag", it.nombre.toString())
                    it.nombre!!.contains(busqueda.text.toString(), ignoreCase = true)
                }
                Log.v("lista", lista.toString())
                recycler.adapter?.notifyDataSetChanged()
            }
        }

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

//    override val coroutineContext: CoroutineContext
//        get() = TODO("Not yet implemented")
}