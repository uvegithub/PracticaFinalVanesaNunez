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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CrearEvento : AppCompatActivity(), CoroutineScope {

    private lateinit var nombre: TextInputEditText
    private lateinit var precio: TextInputEditText
    private lateinit var aforoMax: TextInputEditText
    private lateinit var aforoOcu: TextInputEditText
    private lateinit var bcrear: Button
    private lateinit var bvolver: Button

//    private var url_carta: Uri? = null
    private lateinit var database_ref: DatabaseReference
    private lateinit var storage_ref: StorageReference
    private lateinit var lista_eventos: MutableList<Evento>

    private lateinit var job: Job

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rol_usuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        val this_activity = this
        job = Job()

        nombre=findViewById(R.id.textinputedittextNombreE)
        precio=findViewById(R.id.textinputedittextPrecioE)
        aforoMax=findViewById(R.id.textinputedittextAforoMaxE)
        aforoOcu=findViewById(R.id.textinputedittextAforoOcuE)
        bcrear=findViewById(R.id.buttonE)
        bvolver=findViewById(R.id.button_volverE)

        database_ref = FirebaseDatabase.getInstance().getReference()
        storage_ref = FirebaseStorage.getInstance().getReference()
        lista_eventos = Utilidades.obtenerListaEventos(database_ref)

        bcrear.setOnClickListener {

//            val dateTime = LocalDateTime.now()
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (nombre.text.toString().trim().isEmpty() ||
                precio.text.toString().trim().isEmpty() ||
                aforoMax.text.toString().trim().isEmpty() ||
                aforoOcu.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    applicationContext, "Faltan datos en el " +
                            "formulario", Toast.LENGTH_SHORT
                ).show()

//            } else if (url_carta == null) {
//                Toast.makeText(
//                    applicationContext, "Falta seleccionar el " +
//                            "escudo", Toast.LENGTH_SHORT
//                ).show()
            } else if (Utilidades.existeEvento(lista_eventos, nombre.text.toString().trim())) {
                Toast.makeText(applicationContext, "Esa carta ya existe", Toast.LENGTH_SHORT)
                    .show()
            } else {

                var id_generado: String? = database_ref.child("tienda").child("eventos").push().key
//                sharedPreferences.edit().putString("id_evento", id_generado.toString().trim()).apply()

                //GlobalScope(Dispatchers.IO)
                launch {
//                    val url_carta_firebase =
//                        Utilidades.guardarImagen(storage_ref, id_generado!!, url_carta!!)

                    val androidId =
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

                    Utilidades.escribirEvento(
                        database_ref, id_generado!!,
                        nombre.text.toString().trim(),
                        precio.text.toString().trim().toFloat(),
                        aforoMax.text.toString().trim().toInt(),
                        aforoOcu.text.toString().trim().toInt(),
//                        url_carta_firebase,
                        Estado.CREADO,
                        androidId)

                    Utilidades.tostadaCorrutina(
                        this_activity,
                        applicationContext,
                        "Evento creado con exito"
                    )

                    sharedPreferences.edit().putString("id_evento", id_generado.toString().trim()).apply()

                    val activity = Intent(applicationContext, Ver_cartas::class.java)
                    startActivity(activity)
                }

            }
        }

        bvolver.setOnClickListener {
            val activity = Intent(applicationContext, Ver_cartas::class.java)
            startActivity(activity)
        }

//        imagen.setOnClickListener {
//            accesoGaleria.launch("image/*")
//
//        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

//    private val accesoGaleria = registerForActivityResult(ActivityResultContracts.GetContent())
//    { uri: Uri ->
//        if (uri != null) {
//            url_carta = uri
//            imagen.setImageURI(uri)
//        }
//    }
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