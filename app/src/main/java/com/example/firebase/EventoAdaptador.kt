package com.example.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventoAdaptador(var lista_eventos: MutableList<Evento>):
    RecyclerView.Adapter<EventoAdaptador.EventoViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_eventos

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rol_usuario: String

    var canalId:Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventoAdaptador.EventoViewHolder {
        val vista_item = LayoutInflater.from(parent.context).inflate(R.layout.item_evento,parent,false)
        contexto = parent.context
        return EventoViewHolder(vista_item)
    }

    override fun onBindViewHolder(holder: EventoAdaptador.EventoViewHolder, position: Int) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto)

        val item_actual = lista_filtrada[position]
        holder.nombre.text = item_actual.nombre
        holder.precio.text = item_actual.precio.toString()
        holder.aforoMax.text = item_actual.aforo_max.toString()
        holder.aforoOcu.text = item_actual.aforo_ocupado.toString()

        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        if(rol_usuario=="cliente"){
            holder.apuntarse.setVisibility(View.VISIBLE)
            holder.aceptar.setVisibility(View.INVISIBLE)
        }else{
            holder.apuntarse.setVisibility(View.INVISIBLE)
            holder.aceptar.setVisibility(View.VISIBLE)
        }

        holder.apuntarse.setOnClickListener {
            val activity = Intent(contexto,VerEventos::class.java)
            activity.putExtra("eventos", item_actual)
            mostrar_notificacion(contexto)
        }

        holder.aceptar.setOnClickListener {
            if(holder.aforoOcu.text.toString().toInt()<holder.aforoMax.text.toString().toInt()){
                mostrar_notificacion2(contexto)
                var aforo=holder.aforoOcu.text.toString().toInt()
                aforo+=1
                holder.aforoOcu.text=aforo.toString()
            }
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

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.nombre)
        val precio: TextView = itemView.findViewById(R.id.precio)
        val aforoMax: TextView = itemView.findViewById(R.id.aforoMax)
        var aforoOcu: TextView = itemView.findViewById(R.id.aforoOcu)
        val apuntarse: Button = itemView.findViewById(R.id.apuntarseE)
        val aceptar: ImageView = itemView.findViewById(R.id.icono_aceptarE)

    }

    override fun getFilter(): Filter {
        return  object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val busqueda = p0.toString().lowercase()
                if (busqueda.isEmpty()){
                    lista_filtrada = lista_eventos
                }else {
                    lista_filtrada = (lista_eventos.filter {
                        it.nombre.toString().lowercase().contains(busqueda)
                    }) as MutableList<Evento>
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

    fun mostrar_notificacion(contexto: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            canalId += 1
            var canalNombre = "canal" + canalId.toString()
            val notificationChannel = NotificationChannel(canalId.toString(), canalNombre, NotificationManager.IMPORTANCE_DEFAULT)
            contexto.getSystemService<NotificationManager>()?.createNotificationChannel(notificationChannel)
        }

//        val intento = Intent(contexto, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(contexto, 0, intento, PendingIntent.FLAG_UPDATE_CURRENT)

//        val intento_boton = Intent(contexto, Mi_cesta::class.java)
//        val pendingIntent_boton = PendingIntent.getActivity(contexto, 0, intento_boton, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(contexto, canalId.toString())
            .setSmallIcon(R.drawable.notification_icon_124899)
            .setContentTitle("Notificacion")
            .setContentText("Te has apuntado a un evento. Mi id es "+canalId)
//            .setContentIntent(pendingIntent)
//            .addAction(R.drawable.notification_icon_124899,"Ir a mi cesta", pendingIntent_boton)

        with(contexto.getSystemService<NotificationManager>()){
            this?.notify(1,builder.build())
        }
    }

    fun mostrar_notificacion2(contexto: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            canalId += 1
            var canalNombre = "canal" + canalId.toString()
            val notificationChannel = NotificationChannel(canalId.toString(), canalNombre, NotificationManager.IMPORTANCE_DEFAULT)
            contexto.getSystemService<NotificationManager>()?.createNotificationChannel(notificationChannel)
        }

//        val intento = Intent(contexto, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(contexto, 0, intento, PendingIntent.FLAG_UPDATE_CURRENT)

//        val intento_boton = Intent(contexto, Mi_cesta::class.java)
//        val pendingIntent_boton = PendingIntent.getActivity(contexto, 0, intento_boton, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(contexto, canalId.toString())
            .setSmallIcon(R.drawable.notification_icon_124899)
            .setContentTitle("Notificacion")
            .setContentText("La solicitud es aceptada. Mi id es "+canalId)
//            .setContentIntent(pendingIntent)
//            .addAction(R.drawable.notification_icon_124899,"Ir a mi cesta", pendingIntent_boton)

        with(contexto.getSystemService<NotificationManager>()){
            this?.notify(1,builder.build())
        }
    }
}
