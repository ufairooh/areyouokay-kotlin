package com.example.areyouokay.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getDeteksiModel
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var nama: TextView
    private lateinit var icon_gender: ImageView
    private lateinit var kuesionerButton: CardView
    private lateinit var btnProfil: LinearLayout
    private lateinit var btnArtikel: CardView
    private lateinit var depresi: TextView
    private lateinit var keyakinan: TextView
    private lateinit var penanganan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        nama = findViewById(R.id.nama)
        icon_gender = findViewById(R.id.icon_gender)
        depresi = findViewById(R.id.depresi)
        keyakinan = findViewById(R.id.keyakinan)
        penanganan = findViewById(R.id.penanganan)


        val idUser = intent.getStringExtra("id_user")
        val email_User = intent.getStringExtra("email")
        val nama_User = intent.getStringExtra("nama")


        api.getUser("" + email_User +"","" + nama_User + "", "" + idUser + "").enqueue(object : Callback<getUserModel> {

            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                if(response.isSuccessful){
                    val listdata = response.body()!!.user

                    listdata.forEach{
                        nama.setText("${it.nama}")
                        val gender = "${it.jenis_kelamin}"
                        val id_user = "${it.id_user}"
                        if(gender == "Perempuan"){
                            icon_gender.setImageResource(R.drawable.girl)
                        }else{
                            icon_gender.setImageResource(R.drawable.boy)
                        }
                        api.getLastDeteksi("" + id_user + "").enqueue(object : Callback<getDeteksiModel> {
                            override fun onResponse(call: Call<getDeteksiModel>, response: Response<getDeteksiModel>) {
                                if(response.isSuccessful){
                                    val listdata = response.body()!!.deteksi

                                    listdata.forEach{
                                        val id_depresi = "${it.id_depresi}"
                                        val hasilhitung = "${it.hasil_hitung}"
                                        val hasil = hasilhitung.toDouble()
                                        val hasilpersen = hasil * 100
                                        keyakinan.setText(hasilpersen.toString() + "%")
                                        if(id_depresi == "1"){
                                            depresi.setText("Tidak Depresi")
                                            penanganan.visibility = View.GONE

                                        }
                                        else if(id_depresi == "2"){
                                            depresi.setText("Depresi Ringan")
                                            intentPenanganan("2", id_user)
                                        }
                                        else if (id_depresi == "3"){
                                            depresi.setText("Depresi Sedang")
                                            intentPenanganan("3", id_user)
                                        }
                                        else if(id_depresi == "4"){
                                            depresi.setText("Depresi Berat")
                                            intentPenanganan("4", id_user)
                                        }
                                        else{
                                            depresi.setText("-")
                                            penanganan.visibility = View.GONE
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<getDeteksiModel>, t: Throwable) {
                            }

                        })

                        btnArtikel = findViewById(R.id.btnArtikel)
                        btnArtikel.setOnClickListener {
                            val intent = Intent(this@HomeActivity, ArtikelActivity::class.java)
                            intent.putExtra("id_user", id_user)
                            startActivity(intent)
                        }

                        intentKuesioner(id_user)
                        intentProfil(id_user)
                    }
                }
            }

            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                Log.e("HomeActivity", t.toString())
            }

        })





    }

    private fun intentKuesioner(id_user: String){
        kuesionerButton = findViewById(R.id.btnKuesioner)
        kuesionerButton.setOnClickListener {
            val intent = Intent(this, IntroDeteksiActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

    private fun intentPenanganan(id_depresi: String, id_user: String){
        penanganan = findViewById(R.id.penanganan)
        penanganan.setOnClickListener {
            val intent = Intent(this, PenangananActivity::class.java)
            intent.putExtra("id_depresi", id_depresi)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

    private fun intentProfil(id_user: String){
        btnProfil = findViewById(R.id.btnProfil)
        btnProfil.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

}