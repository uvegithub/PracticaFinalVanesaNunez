package com.example.firebase

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class Editar_carta : AppCompatActivity(), CoroutineScope {

    private lateinit var nombre: TextInputEditText
    private lateinit var precio: TextInputEditText
    private lateinit var disponibilidad: TextInputEditText
    private lateinit var categoria: TextInputEditText
    private lateinit var imagen: ImageView
    private lateinit var beditar: Button
    private lateinit var bvolver: Button

    private var url_imagen: Uri? = null
    private lateinit var database_ref: DatabaseReference
    private lateinit var storage_ref: StorageReference
    private lateinit var lista_cartas: MutableList<Carta>

    private lateinit var job: Job

    private  lateinit var  pojo_carta:Carta

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rol_usuario: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_carta)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        val this_activity = this
        job = Job()

        pojo_carta = intent.getParcelableExtra<Carta>("cartas")!!


        nombre=findViewById(R.id.textinputedittextuNombre)
        precio=findViewById(R.id.textinputedittextPrecio)
        disponibilidad=findViewById(R.id.textinputedittextDisponibilidad)
        categoria=findViewById(R.id.textinputedittextCategoria)
        imagen=findViewById(R.id.imageView)

        beditar=findViewById(R.id.button)
        bvolver=findViewById(R.id.button_volver)

        nombre.setText(pojo_carta.nombre)
        precio.setText(pojo_carta.precio.toString())
        disponibilidad.setText(pojo_carta.disponible)
        categoria.setText(pojo_carta.categoria)


        database_ref = FirebaseDatabase.getInstance().getReference()
        storage_ref = FirebaseStorage.getInstance().getReference()
        lista_cartas = Utilidades.obtenerListaCartas(database_ref)

        Glide.with(applicationContext)
            .load(pojo_carta.imagen)
            .apply(Utilidades.opcionesGlide(applicationContext))
            .transition(Utilidades.transicion)
            .into(imagen)

        beditar.setOnClickListener {

//            val dateTime = LocalDateTime.now()
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (nombre.text.toString().trim().isEmpty() ||
                precio.text.toString().trim().isEmpty() ||
                disponibilidad.text.toString().trim().isEmpty() ||
                categoria.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    applicationContext, "Faltan datos en el " +
                            "formulario", Toast.LENGTH_SHORT
                ).show()

            } else if ( !nombre.text.toString().trim().equals(pojo_carta.nombre) && Utilidades.existeCarta(lista_cartas, nombre.text.toString().trim())) {
                Toast.makeText(applicationContext, "Esa carta ya existe", Toast.LENGTH_SHORT)
                    .show()
            } else {

                //GlobalScope(Dispatchers.IO)
                var url_imagen_firebase = String()
                launch {
                    if(url_imagen == null){
                        url_imagen_firebase = pojo_carta.imagen!!
                    }else{
                        val url_imagen_firebase =
                            Utilidades.guardarImagen(storage_ref, pojo_carta.id!!, url_imagen!!)
                    }

                    val androidId =
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

                    Utilidades.escribirCarta(
                        database_ref, pojo_carta.id!!,
                        nombre.text.toString().trim(),
                        precio.text.toString().trim().toFloat(),
                        disponibilidad.text.toString().trim(),
                        categoria.text.toString().trim(),
                        url_imagen_firebase,
                        Estado.MODIFICADO,
                        androidId
                    )
                    Utilidades.tostadaCorrutina(
                        this_activity,
                        applicationContext,
                        "Carta modificada con exito"
                    )
                    val activity = Intent(applicationContext, Ver_cartas::class.java)
                    startActivity(activity)
                }
            }

        }

        bvolver.setOnClickListener {
            val activity = Intent(applicationContext, Ver_cartas::class.java)
            startActivity(activity)
        }

        imagen.setOnClickListener {
            accesoGaleria.launch("image/*")
        }

    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private val accesoGaleria = registerForActivityResult(ActivityResultContracts.GetContent())
    {uri: Uri ->
        if(uri!=null){
            url_imagen = uri
            imagen.setImageURI(uri)
        }


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

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
            R.id.accion_crear_cartas -> {
                val intent3 = Intent(this, Mi_cesta::class.java)
                startActivity(intent3)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}