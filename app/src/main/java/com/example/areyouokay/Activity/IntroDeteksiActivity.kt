package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.areyouokay.R

class IntroDeteksiActivity : AppCompatActivity() {

    private lateinit var btnMulai : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_deteksi)

        btnMulai = findViewById(R.id.btnMulai)
        val id_user = intent.getStringExtra("id_user")

        btnMulai.setOnClickListener {
            val intent = Intent(this, DeteksiActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val id_user = intent.getStringExtra("id_user")
        val builder = AlertDialog.Builder(this@IntroDeteksiActivity)
        builder.setMessage("Apakah kamu yakin tidak ingin melanjutkan deteksi dini depresi?")
            .setPositiveButton("IYA", {
                    dialogInterface, i -> val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("id_user", id_user)
                startActivity(intent)
                finishAffinity()
            })
            .setNegativeButton("TIDAK", {
                    dialogInterface, i ->
            })
        val dialog = builder.create()
        dialog.show()
    }
}