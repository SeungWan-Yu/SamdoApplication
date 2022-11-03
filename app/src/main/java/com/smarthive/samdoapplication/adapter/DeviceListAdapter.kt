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
import com.smarthive.samdoapplication.model.DeviceData


class DeviceListAdapter(val mContext:Context, val deviceList: List<DeviceData>, private val clickListener: (DeviceData) -> Unit) :
    RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    interface ItemClick{ //인터페이스
        fun onLongClick(view: View, position: Int)
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflate = LayoutInflater.from(parent.context)
        val view = inflate.inflate(R.layout.device_listview, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mContext,deviceList[position], clickListener)
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
        return deviceList.size
        Log.e("e",deviceList.size.toString())
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val plasma = itemView?.findViewById<TextView>(R.id.plasma)
        val port = itemView?.findViewById<TextView>(R.id.port)
        val llayout = itemView?.findViewById<LinearLayout>(R.id.llayout)
        val protocol = itemView?.findViewById<TextView>(R.id.protocol)
        @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")

        fun bind(mContext: Context, device: DeviceData, clickListener: (DeviceData) -> Unit) {

            if (plasma != null) {
                if (device != null) {
                    plasma.text = device.devicename
                }
            }
            if (port != null) {
                if (device != null) {
                    port.text = device.port.toString()
                }
            }
            if (protocol != null) {
                if (device != null) {
                    protocol.text = device.id
                }
            }
           itemView.setOnClickListener {
               if (device != null) {
                   clickListener(device)
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