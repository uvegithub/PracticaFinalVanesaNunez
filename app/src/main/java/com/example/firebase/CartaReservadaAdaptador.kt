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
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartaReservadaAdaptador(var lista_cartas_reservadas: MutableList<CartaReservada>):
    RecyclerView.Adapter<CartaReservadaAdaptador.CartaReservadaViewHolder>(), Filterable {
    private lateinit var contexto: Context
    private var lista_filtrada = lista_cartas_reservadas

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rol_usuario: String

    var canalId:Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartaReservadaAdaptador.CartaReservadaViewHolder {
        val vista_item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carta_reservada, parent, false)
        contexto = parent.context
        return CartaReservadaAdaptador.CartaReservadaViewHolder(vista_item)
    }

    override fun onBindViewHolder(
        holder: CartaReservadaAdaptador.CartaReservadaViewHolder,
        position: Int
    ) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto)

        val item_actual = lista_filtrada[position]
        holder.id_c_reservada.text = item_actual.id_reserva_carta
        holder.idcarta.text = item_actual.id_carta
        holder.idusuario.text = item_actual.id_usuario
        holder.estado_c.text = item_actual.estado

        val URL: String? = when (item_actual.imagen) {
            "" -> null
            else -> item_actual.imagen
        }

        Glide.with(contexto)
            .load(URL)
            .apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion)
            .into(holder.miniatura)


        rol_usuario = sharedPreferences.getString("usuario", "administrador").toString()

        if (rol_usuario == "cliente") {
            holder.accept.setVisibility(View.INVISIBLE)
        } else {
            holder.accept.setVisibility(View.VISIBLE)
        }


        holder.accept.setOnClickListener {
            if (holder.estado_c.text != "Preparado") {
                mostrar_notificacion(contexto)
                val activity = Intent(contexto, Mi_cesta::class.java)
                activity.putExtra("carta_comprada", item_actual)
                sharedPreferences.edit().putString("estado", "Preparado").apply()


            }

        }

    }

    override fun getItemCount(): Int = lista_filtrada.size

    class CartaReservadaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val miniatura: ImageView = itemView.findViewById(R.id.imagen_defecto)
        val id_c_reservada: TextView = itemView.findViewById(R.id.id_carta_reservada)
        val idcarta: TextView = itemView.findViewById(R.id.id_carta)
        val idusuario: TextView = itemView.findViewById(R.id.id_usuario)
        val estado_c: TextView = itemView.findViewById(R.id.estado)
        val accept: ImageView = itemView.findViewById(R.id.icono_aceptar)
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

        val intento_boton = Intent(contexto, Ver_cartas::class.java)
        val pendingIntent_boton = PendingIntent.getActivity(contexto, 0, intento_boton, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(contexto, canalId.toString())
            .setSmallIcon(R.drawable.notification_icon_124899)
            .setContentTitle("Notificacion")
            .setContentText("El pedido ya está preparado. Mi id es "+canalId)
//            .setContentIntent(pendingIntent)
            .addAction(R.drawable.notification_icon_124899,"Ir al catalogo", pendingIntent_boton)

        with(contexto.getSystemService<NotificationManager>()){
            this?.notify(1,builder.build())
        }
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
