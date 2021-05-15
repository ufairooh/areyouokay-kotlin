package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getPenangananModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PenangananActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var judul_penanganan : TextView
    private lateinit var isi_penanganan : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penanganan)

        judul_penanganan = findViewById(R.id.judul_penanganan)
        isi_penanganan = findViewById(R.id.isi_penanganan)

        val idDepresi = intent.getStringExtra("id_depresi")

        api.gePenanganan("" + idDepresi +"").enqueue(object : Callback<getPenangananModel> {
            override fun onResponse(call: Call<getPenangananModel>, response: Response<getPenangananModel>) {
                if(response.isSuccessful){
                        judul_penanganan.setText("${response.body()?.judul}")
                        isi_penanganan.setText("${response.body()?.isi}")
                }
            }

            override fun onFailure(call: Call<getPenangananModel>, t: Throwable) {
                Log.e("PenangananActivity", t.toString())
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
}