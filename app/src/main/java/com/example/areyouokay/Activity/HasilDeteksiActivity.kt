package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.R
import java.math.RoundingMode
import java.text.DecimalFormat

class HasilDeteksiActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var keyakinanDepresi : TextView
    private lateinit var iconDepresi : ImageView
    private lateinit var tingkatDepresi : TextView
    private lateinit var btnHome : TextView
    private lateinit var kesimpulan : TextView
    private lateinit var btnPenanganan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_deteksi)

        keyakinanDepresi = findViewById(R.id.keyakinanDepresi)
        iconDepresi = findViewById(R.id.iconDepresi)
        tingkatDepresi = findViewById(R.id.tingkatDepresi)
        btnHome = findViewById(R.id.btnHome)
        btnPenanganan = findViewById(R.id.btnPenanganan)
        kesimpulan = findViewById(R.id.kesimpulan)

        val id_user = intent.getStringExtra("id_user")
        val hasil_hitung = intent.getStringExtra("hasil_hitung")!!.toDouble()
        val id_depresi = intent.getStringExtra("id_depresi")

        val keyakinan = hasil_hitung * 100
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING
        val hasilformat = df.format(keyakinan)
        keyakinanDepresi.setText(hasilformat.toString() + "%")

        if(id_depresi == "1"){
            iconDepresi.setImageResource(R.drawable.depresi1)
            tingkatDepresi.setText("Tidak Depresi")
            btnPenanganan.setText("Artikel Depresi")
            kesimpulan.setText(hasilformat.toString() + "% kemungkinan kamu tidak mengalami depresi. " +
                    "Namun, jangan senang dulu ya karena meskipun saat ini kamu tidak mengalami depresi, suatu saat kamu ada kemungkinan mengalami depresi." +
                    " Yuk cek artikel mengenai depresi agar kamu lebih memahami tentang depresi")
            intentArtikel(id_user)

        }
        else if(id_depresi == "2"){
            iconDepresi.setImageResource(R.drawable.depresi2)
            tingkatDepresi.setText("Depresi Ringan")
            kesimpulan.setText(hasilformat.toString() + "% kemungkinan kamu mengalami depresi ringan. " +
                    "Depresi ringan jika tidak ditangani dapat menyebabkan depresi sedang bahkan depresi berat." +
                    " Untuk menangani depresi ringan yuk cek tipsnya dengan masuk ke halaman penanganan depresi!")
            intentPenanganan("2", id_user)
        }
        else if (id_depresi == "3"){
            iconDepresi.setImageResource(R.drawable.depresi3)
            tingkatDepresi.setText("Depresi Sedang")
            kesimpulan.setText(hasilformat.toString() + "% kemungkinan kamu mengalami depresi sedang. " +
                    "Depresi sedang jika tidak ditangani dapat menyebabkan depresi berat." +
                    " Untuk menangani depresi sedang yuk cek tipsnya dengan masuk ke halaman penanganan depresi!")
            intentPenanganan("3", id_user)
        }
        else{
            iconDepresi.setImageResource(R.drawable.depresi4)
            tingkatDepresi.setText("Depresi Berat")
            kesimpulan.setText(hasilformat.toString() + "% kemungkinan kamu mengalami depresi berat. " +
                    "Depresi berat akan sangat berbahaya. " +
                    " Untuk menangani depresi berat kamu bisa menghubungi psikolog dan kamu bisa cek tips untuk menanganinnya dengan masuk ke halaman penanganan depresi!")
            intentPenanganan("4", id_user)
        }

        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun intentPenanganan(id_depresi: String, id_user: String){
        btnPenanganan = findViewById(R.id.btnPenanganan)
        btnPenanganan.setOnClickListener {
            val intent = Intent(this, PenangananActivity::class.java)
            intent.putExtra("id_depresi", id_depresi)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

    private fun intentArtikel(id_user: String){
        btnPenanganan = findViewById(R.id.btnPenanganan)
        btnPenanganan.setOnClickListener {
            val intent = Intent(this, ArtikelActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
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