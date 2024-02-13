package com.example.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CartaAdaptador (var lista_cartas: MutableList<Carta>):
    RecyclerView.Adapter<CartaAdaptador.CartaViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_cartas

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rol_usuario: String



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartaAdaptador.CartaViewHolder {
        val vista_item = LayoutInflater.from(parent.context).inflate(R.layout.item_carta,parent,false)
        contexto = parent.context
        return CartaViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: CartaAdaptador.CartaViewHolder, position: Int) {
        val item_actual = lista_filtrada[position]
        holder.nombre.text = item_actual.nombre
        holder.precio.text = item_actual.precio.toString()
        holder.categoria.text = item_actual.nombre
        holder.disponibilidad.text = item_actual.nombre

        val URL:String? = when(item_actual.imagen){
            ""-> null
            else -> item_actual.imagen
        }

        Glide.with(contexto)
            .load(URL)
            .apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion)
            .into(holder.miniatura)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto)
        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        if(rol_usuario=="cliente"){
            holder.imagen_comprar.setVisibility(View.VISIBLE)
            holder.editar.setVisibility(View.INVISIBLE)
        }else{
            holder.imagen_comprar.setVisibility(View.INVISIBLE)
            holder.editar.setVisibility(View.VISIBLE)
        }

        holder.editar.setOnClickListener {
            val activity = Intent(contexto,Editar_carta::class.java)
            activity.putExtra("cartas", item_actual)
            contexto.startActivity(activity)
        }

        holder.imagen_comprar.setOnClickListener {
            val activity = Intent(contexto,Mi_cesta::class.java)
            activity.putExtra("cartas", item_actual)
        }

//        holder.eliminar.setOnClickListener {
//            val  db_ref = FirebaseDatabase.getInstance().getReference()
//            val sto_ref = FirebaseStorage.getInstance().getReference()
//
//            val androidId =
//                Settings.Secure.getString(contexto.contentResolver, Settings.Secure.ANDROID_ID)
//
//            lista_filtrada.remove(item_actual)
//            sto_ref.child("tienda").child("cartas")
//                .child(item_actual.id!!).delete()
//            db_ref.child("tienda").child("cartas")
//                .child(item_actual.id!!).removeValue()
//
//            Toast.makeText(contexto,"Carta borrada con exito", Toast.LENGTH_SHORT).show()
//            Toast.makeText(contexto,"Carta borrada con exito", Toast.LENGTH_SHORT).show()
//        }

    }

    override fun getItemCount(): Int = lista_filtrada.size

    class CartaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val miniatura: ImageView = itemView.findViewById(R.id.item_miniatura)
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val categoria: TextView = itemView.findViewById(R.id.categoria)
        val disponibilidad: TextView = itemView.findViewById(R.id.disponibilidad)
        val editar: ImageView = itemView.findViewById(R.id.editar)

        var imagen_comprar: ImageView = itemView.findViewById(R.id.comprar)
    }

    override fun getFilter(): Filter {
        return  object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val busqueda = p0.toString().lowercase()
                if (busqueda.isEmpty()){
                    lista_filtrada = lista_cartas
                }else {
                    lista_filtrada = (lista_cartas.filter {
                        it.nombre.toString().lowercase().contains(busqueda)
                    }) as MutableList<Carta>
                }

                val filterResults = FilterResults()
                filterResults.values = lista_filtrada
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }
}