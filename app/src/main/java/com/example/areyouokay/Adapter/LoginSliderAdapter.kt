package com.example.areyouokay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LoginSliderAdapter(private val loginSliders: List<LoginSlider>) :
RecyclerView.Adapter<LoginSliderAdapter.LoginSliderViewHolder>(){

    inner class  LoginSliderViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val textTitle = view.findViewById<TextView>(R.id.textTitle)
        private val textDescription = view.findViewById<TextView>(R.id.textDescription)
        private val imageIcon = view.findViewById<ImageView>(R.id.imageSlideIcon)

        fun bind(loginSlider: LoginSlider){
            textTitle.text = loginSlider.title
            textDescription.text = loginSlider.description
            imageIcon.setImageResource(loginSlider.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginSliderViewHolder {
        return LoginSliderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.slide_login_container,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: LoginSliderViewHolder, position: Int) {
        holder.bind(loginSliders[position])
    }

    override fun getItemCount(): Int {
        return loginSliders.size
    }
}