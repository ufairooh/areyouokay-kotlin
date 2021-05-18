package com.example.areyouokay.Adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.areyouokay.Activity.ArtikelActivity
import com.example.areyouokay.Activity.IsiArtikelActivity
import com.example.areyouokay.Model.getArtikelModel
import com.example.areyouokay.Model.getDeteksiModel
import com.example.areyouokay.R
import java.net.URL

class ArtikelAdapter(val artikel: ArrayList<getArtikelModel>): RecyclerView.Adapter<ArtikelAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
              LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_artikel, parent, false)

    )

    override fun onBindViewHolder(holder: ArtikelAdapter.ViewHolder, position: Int) {
        val data = artikel[position]
        holder.judulArtikel.text = data.judul
        Glide.with(holder.artikel.context).load("https://res.cloudinary.com/dddl4nlew/"+ data.image).into(holder.icon_artikel)
        holder.artikel.setOnClickListener {
            val intent = Intent(holder.artikel.context, IsiArtikelActivity::class.java)
            intent.putExtra("id_artikel", data.id)
            holder.artikel.context.startActivity(intent)

        }
    }

    override fun getItemCount() = artikel.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val judulArtikel = view.findViewById<TextView>(R.id.judulArtikel)
        val artikel = view.findViewById<LinearLayout>(R.id.artikel)
        val icon_artikel = view.findViewById<ImageView>(R.id.icon_artikel)
    }

    public fun setData(data: List<getArtikelModel>){
        artikel.clear()
        artikel.addAll(data)
        notifyDataSetChanged()
    }

    public fun setID(id_user: String) :String{
        val id = id_user
        return id
    }
}