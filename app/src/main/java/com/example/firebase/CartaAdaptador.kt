package com.example.firebase

import android.content.Context
import android.content.Intent
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

class CartaAdaptador (var lista_cartas: MutableList<Carta>, private val listener: View.OnClickListener):
    RecyclerView.Adapter<CartaAdaptador.CartaViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada_categoria = lista_cartas
    private var lista_filtrada_nombre = lista_cartas

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartaAdaptador.CartaViewHolder {
        val vista_item = LayoutInflater.from(parent.context).inflate(R.layout.item_carta,parent,false)
        contexto = parent.context
        return CartaViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: CartaAdaptador.CartaViewHolder, categoria: Int, nombre:String) {
        val item_actual = lista_filtrada_categoria[categoria]
        holder.nombre.text = item_actual.nombre
        holder.precio.text = item_actual.precio
        holder.disponibilidad.text = item_actual.disponible
        holder.anio.text = item_actual.anio_fundacion.toString()
        holder.dia.text = item_actual.fecha_creacion.toString()




        val URL:String? = when(item_actual.escudo){
            ""-> null
            else -> item_actual.escudo
        }

        Glide.with(contexto)
            .load(URL)
            .apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion)
            .into(holder.miniatura)

        holder.editar.setOnClickListener {
            val activity = Intent(contexto,EditarCasa::class.java)
            activity.putExtra("casas", item_actual)
            contexto.startActivity(activity)
        }

        holder.eliminar.setOnClickListener {
            val  db_ref = FirebaseDatabase.getInstance().getReference()
            val sto_ref = FirebaseStorage.getInstance().getReference()

            val androidId =
                Settings.Secure.getString(contexto.contentResolver, Settings.Secure.ANDROID_ID)

            lista_filtrada.remove(item_actual)
            sto_ref.child("howarts").child("casas")
                .child(item_actual.id!!).delete()
            db_ref.child("howarts").child("casas")
                .child(item_actual.id!!).removeValue()

            Toast.makeText(contexto,"Club borrado con exito", Toast.LENGTH_SHORT).show()
        }




    }

    override fun getItemCount(): Int = lista_filtrada.size

    class CartaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val miniatura: ImageView = itemView.findViewById(R.id.item_miniatura)
        val nombre: TextView = itemView.findViewById(R.id.item_nombre)
        val fundador: TextView = itemView.findViewById(R.id.item_fundador)
        val dia: TextView = itemView.findViewById(R.id.item_dia)
        val anio: TextView = itemView.findViewById(R.id.item_anio)
        val estrellas: TextView = itemView.findViewById(R.id.item_estrellas)
        val editar: ImageView = itemView.findViewById(R.id.item_editar)
        val eliminar: ImageView = itemView.findViewById(R.id.item_borrar)
    }

    override fun getFilter(): Filter {
        return  object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val busqueda = p0.toString().lowercase()
                if (busqueda.isEmpty()){
                    lista_filtrada = lista_casas
                }else {
                    lista_filtrada = (lista_casas.filter {
                        it.nombre.toString().lowercase().contains(busqueda)
                    }) as MutableList<Pojo_casa>
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