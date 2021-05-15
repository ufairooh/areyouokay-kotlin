package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Adapter.ArtikelAdapter
import com.example.areyouokay.Model.getArtikelModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ArtikelActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var artikelAdapter: ArtikelAdapter
    private lateinit var recyclerArtikel: RecyclerView
    private lateinit var artikel : LinearLayout
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)

        val builder = AlertDialog.Builder(this@ArtikelActivity)
        inflater = this@ArtikelActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

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
                Callback<List<getArtikelModel>> {
            override fun onResponse(call: Call<List<getArtikelModel>>, response: Response<List<getArtikelModel>>) {
                if (response.isSuccessful) {
                    val id_user = intent.getStringExtra("id_user")
                    val listData = response.body()!!
                    artikelAdapter.setData(listData)
                    artikelAdapter.setID(id_user)
                    dialog.dismiss()


                }
            }

            override fun onFailure(call: Call<List<getArtikelModel>>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getArtikel().enqueue(object :
                            Callback<List<getArtikelModel>> {
                        override fun onResponse(call: Call<List<getArtikelModel>>, response: Response<List<getArtikelModel>>) {
                            if (response.isSuccessful) {
                                val id_user = intent.getStringExtra("id_user")
                                val listData = response.body()!!
                                artikelAdapter.setData(listData)
                                artikelAdapter.setID(id_user)
                                dialog.dismiss()


                            }
                        }

                        override fun onFailure(call: Call<List<getArtikelModel>>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@ArtikelActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }


                    })
                }
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