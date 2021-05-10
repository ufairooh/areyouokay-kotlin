package com.example.areyouokay.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.areyouokay.Model.getDeteksiModel
import com.example.areyouokay.R

class HistoryAdapter(val history: ArrayList<getDeteksiModel.Data>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_history, parent, false)
    )

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val data = history[position]
        holder.timestamp.text = data.tanggal
        val depresi = data.id_depresi
        if (depresi == "1"){
            holder.tingkat_depresi.text = "Tidak Depresi"
            holder.icon_depresi.setImageResource(R.drawable.depresi1)
        }
        else if(depresi == "2"){
            holder.tingkat_depresi.text = "Depresi Ringan"
            holder.icon_depresi.setImageResource(R.drawable.depresi2)
        }
        else if(depresi == "3"){
            holder.tingkat_depresi.text = "Depresi Sedang"
            holder.icon_depresi.setImageResource(R.drawable.depresi3)
        }
        else if(depresi == "4"){
            holder.tingkat_depresi.text = "Depresi Berat"
            holder.icon_depresi.setImageResource(R.drawable.depresi4)
        }

    }

    override fun getItemCount() = history.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val timestamp = view.findViewById<TextView>(R.id.timestamp)
        val tingkat_depresi = view.findViewById<TextView>(R.id.tingkat_depresi)
        val icon_depresi = view.findViewById<ImageView>(R.id.icon_depresi)
    }

    public fun setData(data: List<getDeteksiModel.Data>){
        history.clear()
        history.addAll(data)
        notifyDataSetChanged()
    }
}