package com.smarthive.samdoapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.model.SensorData


class SensorListAdapter(val mContext:Context, val sensorList: List<SensorData>, private val clickListener: (SensorData) -> Unit) :
    RecyclerView.Adapter<SensorListAdapter.ViewHolder>() {

    interface ItemClick{ //인터페이스
        fun onLongClick(view: View, position: Int)
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflate = LayoutInflater.from(parent.context)
        val view = inflate.inflate(R.layout.sensor_listview, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mContext,sensorList[position], clickListener)
        if (itemClick != null){
//            holder.delete?.setOnClickListener {
//                itemClick?.onClick(it, position)
//            }
            holder.llayout?.setOnLongClickListener(View.OnLongClickListener {
                itemClick?.onLongClick(it, position)
                return@OnLongClickListener true
            })
        }

    }

    override fun getItemCount(): Int {
        return sensorList.size
        Log.e("e",sensorList.size.toString())
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val llayout = itemView?.findViewById<LinearLayout>(R.id.s_llayout)
        val ssensor = itemView?.findViewById<TextView>(R.id.sensor)
        val port = itemView?.findViewById<TextView>(R.id.s_port)
        val protocol = itemView?.findViewById<TextView>(R.id.s_protocol)
        val memory = itemView?.findViewById<TextView>(R.id.s_memory)
        val lati = itemView?.findViewById<TextView>(R.id.s_lati)
        val longi = itemView?.findViewById<TextView>(R.id.s_longi)
        val addr = itemView?.findViewById<TextView>(R.id.s_addr)
        @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")

        fun bind(mContext: Context, sensor: SensorData, clickListener: (SensorData) -> Unit) {

            if (ssensor != null) {
                if (sensor != null) {
                    ssensor.text = sensor.sensorname
                }
            }
            if (port != null) {
                if (sensor != null) {
                    port.text = sensor.port
                }
            }
            if (protocol != null) {
                if (sensor != null) {
                    protocol.text = "0000"
                }
            }
            if (memory != null) {
                if (sensor != null) {
                    memory.text = sensor.memory.toString()
                }
            }
            if (lati != null) {
                if (sensor != null) {
                    lati.text = sensor.latitude
                }
            }
            if (longi != null) {
                if (sensor != null) {
                    longi.text = sensor.longitude
                }
            }

            if (addr != null){
                if (sensor != null) {
                    addr.text = sensor.addr
                }
            }
           itemView.setOnClickListener {
               if (sensor != null) {
                   clickListener(sensor)
               }
           }
        }

    }
}

//fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
//    itemView.setOnClickListener {
//        event.invoke(adapterPosition, itemViewType)
//    }
//    return this
//}