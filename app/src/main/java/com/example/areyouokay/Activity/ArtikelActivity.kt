package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Adapter.ArtikelAdapter
import com.example.areyouokay.Model.getArtikelModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtikelActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var artikelAdapter: ArtikelAdapter
    private lateinit var recyclerArtikel: RecyclerView
    private lateinit var artikel : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)

        setupList()





    }

    override fun onStart() {
        super.onStart()
        getArtikel()
    }

    private fun setupList(){
        recyclerArtikel = findViewById(R.id.recycler)
        artikelAdapter = ArtikelAdapter(arrayListOf())
        recyclerArtikel.adapter = artikelAdapter
    }

    private fun getArtikel(){
        api.getArtikel().enqueue(object :
                Callback<getArtikelModel> {
            override fun onResponse(call: Call<getArtikelModel>, response: Response<getArtikelModel>) {
                if (response.isSuccessful) {
                    val id_user = intent.getStringExtra("id_user")
                    val listData = response.body()!!.artikel
                    artikelAdapter.setData(listData)
                    artikelAdapter.setID(id_user)


                }
            }

            override fun onFailure(call: Call<getArtikelModel>, t: Throwable) {
            }


        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val id_user = intent.getStringExtra("id_user")
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("id_user", id_user)
        startActivity(intent)
        finishAffinity()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val idUser = intent.getStringExtra("id_user")
        outState.putString("id_user", idUser)
        super.onSaveInstanceState(outState)
    }
}