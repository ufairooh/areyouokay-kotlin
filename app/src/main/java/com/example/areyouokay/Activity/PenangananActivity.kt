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
import com.example.areyouokay.Model.getPenangananModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class PenangananActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var judul_penanganan : TextView
    private lateinit var isi_penanganan : TextView
    private lateinit var img_penanganan: ImageView
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penanganan)

        val builder = AlertDialog.Builder(this@PenangananActivity)
        inflater = this@PenangananActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        judul_penanganan = findViewById(R.id.judul_penanganan)
        isi_penanganan = findViewById(R.id.isi_penanganan)
        img_penanganan = findViewById(R.id.img_penanganan)

        val idDepresi = intent.getStringExtra("id_depresi")

        api.gePenanganan("" + idDepresi +"").enqueue(object : Callback<getPenangananModel> {
            override fun onResponse(call: Call<getPenangananModel>, response: Response<getPenangananModel>) {
                if(response.isSuccessful){
                        judul_penanganan.setText("${response.body()?.judul}")
                        isi_penanganan.setText("${response.body()?.isi}")
                        Glide.with(this@PenangananActivity).load("https://res.cloudinary.com/dddl4nlew/${response.body()?.image}").into(img_penanganan)
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<getPenangananModel>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.gePenanganan("" + idDepresi +"").enqueue(object : Callback<getPenangananModel> {
                        override fun onResponse(call: Call<getPenangananModel>, response: Response<getPenangananModel>) {
                            if(response.isSuccessful){
                                judul_penanganan.setText("${response.body()?.judul}")
                                isi_penanganan.setText("${response.body()?.isi}")
                                Glide.with(this@PenangananActivity).load("https://res.cloudinary.com/dddl4nlew/${response.body()?.image}").into(img_penanganan)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<getPenangananModel>, t: Throwable) {
                            dialog.dismiss()
                            if(t is SocketTimeoutException){
                                Toast.makeText(this@PenangananActivity,"try again", Toast.LENGTH_LONG).show()
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
}