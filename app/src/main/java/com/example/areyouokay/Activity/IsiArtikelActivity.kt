package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getArtikelModel
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class IsiArtikelActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var judulArtikel: TextView
    private lateinit var isi_artikel: TextView
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater
    private lateinit var img_artikel: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_artikel)

        val builder = AlertDialog.Builder(this@IsiArtikelActivity)
        inflater = this@IsiArtikelActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        judulArtikel = findViewById(R.id.judulArtikel)
        isi_artikel = findViewById(R.id.isi_artikel)
        img_artikel = findViewById(R.id.img_artikel)

        val id_artikel = intent.getStringExtra("id_artikel")

        api.getIsiArtikel("" + id_artikel +"").enqueue(object : Callback<getArtikelModel> {
            override fun onResponse(call: Call<getArtikelModel>, response: Response<getArtikelModel>) {
                if(response.isSuccessful){
                        judulArtikel.setText("${response.body()?.judul}")
                        isi_artikel.setText("${response.body()?.isi}")
                        Glide.with(this@IsiArtikelActivity).load("https://res.cloudinary.com/dddl4nlew/${response.body()?.image}").into(img_artikel)
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<getArtikelModel>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getIsiArtikel("" + id_artikel +"").enqueue(object : Callback<getArtikelModel> {
                        override fun onResponse(call: Call<getArtikelModel>, response: Response<getArtikelModel>) {
                            if(response.isSuccessful){
                                judulArtikel.setText("${response.body()?.judul}")
                                isi_artikel.setText("${response.body()?.isi}")
                                Glide.with(this@IsiArtikelActivity).load("https://res.cloudinary.com/dddl4nlew/${response.body()?.image}").into(img_artikel)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<getArtikelModel>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@IsiArtikelActivity,"try again", Toast.LENGTH_LONG).show()
                            }
                        }


                    })
                }
            }


        })
    }
}