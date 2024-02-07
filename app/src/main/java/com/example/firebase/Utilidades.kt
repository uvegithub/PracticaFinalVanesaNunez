package com.example.firebase

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Utilidades {

    companion object{
        fun existeCliente(clientes : List<Usuario>, login:String):Boolean{
            return clientes.any{ it.login!!.lowercase()==login.lowercase()}
        }


        fun obtenerListaClientes(database_ref: DatabaseReference):MutableList<Usuario>{
            var lista = mutableListOf<Usuario>()

            database_ref.child("tienda")
                .child("usuarios")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{hijo : DataSnapshot ->
                            val pojo_usuario = hijo.getValue(Usuario::class.java)
                            lista.add(pojo_usuario!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }
                })

            return lista
        }

        fun escribirCliente(db_ref: DatabaseReference, id: String, login:String, password:String, tipo:String, estado_notificacion: Int, user_notificacion: String)=
            db_ref.child("tienda").child("usuarios").child(id).setValue(Usuario(
                id,
                login,
                password,
                tipo,
                estado_notificacion,
                user_notificacion,
            ))

        fun tostadaCorrutina(activity: AppCompatActivity, contexto: Context, texto:String){
            activity.runOnUiThread{
                Toast.makeText(
                    contexto,
                    texto,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

//        fun animacion_carga(contexto: Context): CircularProgressDrawable {
//            val animacion = CircularProgressDrawable(contexto)
//            animacion.strokeWidth = 5f
//            animacion.centerRadius = 30f
//            animacion.start()
//            return animacion
//        }


//        val transicion = DrawableTransitionOptions.withCrossFade(500)
//
//        fun opcionesGlide(context: Context): RequestOptions {
//            val options = RequestOptions()
//                .placeholder(animacion_carga(context))
//                .fallback(R.drawable.hogwartscrest)
//                .error(R.drawable.kisspng_http_404_computer_icons_clip_art_5afe161a542093_5617268715266012423446)
//            return options
//        }
    }
}