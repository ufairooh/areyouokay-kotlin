package com.example.areyouokay.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.areyouokay.Activity.ArtikelActivity
import com.example.areyouokay.Activity.IsiArtikelActivity
import com.example.areyouokay.Model.getArtikelModel
import com.example.areyouokay.Model.getDeteksiModel
import com.example.areyouokay.R

class ArtikelAdapter(val artikel: ArrayList<getArtikelModel.Data>): RecyclerView.Adapter<ArtikelAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
              LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_artikel, parent, false)

    )

    override fun onBindViewHolder(holder: ArtikelAdapter.ViewHolder, position: Int) {
        val data = artikel[position]
        holder.judulArtikel.text = data.judul
        holder.artikel.setOnClickListener {
            val intent = Intent(holder.artikel.context, IsiArtikelActivity::class.java)
            intent.putExtra("id_artikel", data.id_artikel)
            holder.artikel.context.startActivity(intent)

        }
    }

    override fun getItemCount() = artikel.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val judulArtikel = view.findViewById<TextView>(R.id.judulArtikel)
        val artikel = view.findViewById<LinearLayout>(R.id.artikel)
    }

    public fun setData(data: List<getArtikelModel.Data>){
        artikel.clear()
        artikel.addAll(data)
        notifyDataSetChanged()
    }

    public fun setID(id_user: String) :String{
        val id = id_user
        return id
    }
}