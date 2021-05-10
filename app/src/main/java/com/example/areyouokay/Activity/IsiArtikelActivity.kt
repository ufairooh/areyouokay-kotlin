package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getArtikelModel
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IsiArtikelActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var judulArtikel: TextView
    private lateinit var isi_artikel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_artikel)

        judulArtikel = findViewById(R.id.judulArtikel)
        isi_artikel = findViewById(R.id.isi_artikel)

        val id_artikel = intent.getStringExtra("id_artikel")

        api.getIsiArtikel("" + id_artikel +"").enqueue(object : Callback<getArtikelModel> {
            override fun onResponse(call: Call<getArtikelModel>, response: Response<getArtikelModel>) {
                if(response.isSuccessful){
                    val listdata = response.body()!!.artikel

                    listdata.forEach{
                        judulArtikel.setText("${it.judul}")
                        isi_artikel.setText("${it.isi}")

                    }
                }
            }

            override fun onFailure(call: Call<getArtikelModel>, t: Throwable) {
            }


        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val id_user = intent.getStringExtra("id_user")
        val intent = Intent(this, ArtikelActivity::class.java)
        intent.putExtra("id_user", id_user)
        startActivity(intent)
    }
}