package com.example.areyouokay.Activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getDeteksiModel
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.Model.postUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.Period

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
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val builder = AlertDialog.Builder(this@HomeActivity)
        inflater = this@HomeActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        nama = findViewById(R.id.nama)
        icon_gender = findViewById(R.id.icon_gender)
        depresi = findViewById(R.id.depresi)
        keyakinan = findViewById(R.id.keyakinan)
        penanganan = findViewById(R.id.penanganan)


        val idUser = intent.getStringExtra("id_user")


        api.getUser("" + idUser +"").enqueue(object : Callback<getUserModel> {
            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                        val name = "${response.body()?.nama}"
                        nama.setText(name)
                        val gender = "${response.body()?.jenis_kelamin}"
                        val id_user = "${response.body()?.id}"
                        val ttl = "${response.body()?.ttl}"
                        val tgl = LocalDate.parse(ttl)
                        val today = LocalDate.now()
                        var diff = Period.between(tgl, today)
                        val umur = "${response.body()?.umur}"
                        val pekerjaan = "${response.body()?.pekerjaan}"
                        val email = "${response.body()?.email}"

                        if(diff.years.toString() != umur){
                            api.updateUser(
                                    "" + idUser + "",
                                    ""  + name + "",
                                    "" + email + "",
                                    "" + ttl + "",
                                    "" + gender + "",
                                    "" + pekerjaan + "", "" + diff.years.toString() + ""
                            ).enqueue(object : Callback<postUserModel>{
                                override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {

                                }

                                override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                    if (t is SocketTimeoutException) {
                                        api.updateUser(
                                                "" + idUser + "",
                                                ""  + name + "",
                                                "" + email + "",
                                                "" + ttl + "",
                                                "" + gender + "",
                                                "" + pekerjaan + "", "" + diff.years.toString() + ""
                                        ).enqueue(object : Callback<postUserModel>{
                                            override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {

                                            }

                                            override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                                if (t is SocketTimeoutException) {
                                                    Toast.makeText(this@HomeActivity,"timeout", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        })
                                    }
                                }
                            })
                        }

                        if(gender == "Perempuan"){
                            icon_gender.setImageResource(R.drawable.girl)
                        }else{
                            icon_gender.setImageResource(R.drawable.boy)
                        }

                api.getLastDeteksi("" + id_user + "").enqueue(object : Callback<getDeteksiModel> {
                            override fun onResponse(call: Call<getDeteksiModel>, response: Response<getDeteksiModel>) {
                                if(response.isSuccessful){
                                        val id_depresi = "${response.body()?.tingkatdepresi_id}"
                                        if(id_depresi == "1"){
                                            val hasilhitung = "${response.body()?.hasil_hitung}"
                                            val hsl = hasilhitung.toDouble()
                                            val hasilpersen = hsl * 100
                                            val df = DecimalFormat("#.###")
                                            df.roundingMode = RoundingMode.CEILING
                                            val hasilformat = df.format(hasilpersen)
                                            keyakinan.setText(hasilformat.toString() + "%")
                                            depresi.setText("Tidak Depresi")
                                            penanganan.visibility = View.GONE

                                        }
                                        else if(id_depresi == "2"){
                                            val hasilhitung = "${response.body()?.hasil_hitung}"
                                            val hsl = hasilhitung.toDouble()
                                            val hasilpersen = hsl * 100
                                            val df = DecimalFormat("#.###")
                                            df.roundingMode = RoundingMode.CEILING
                                            val hasilformat = df.format(hasilpersen)
                                            keyakinan.setText(hasilformat.toString() + "%")
                                            depresi.setText("Depresi Ringan")
                                            intentPenanganan("2", id_user)
                                        }
                                        else if (id_depresi == "3"){
                                            val hasilhitung = "${response.body()?.hasil_hitung}"
                                            val hsl = hasilhitung.toDouble()
                                            val hasilpersen = hsl * 100
                                            val df = DecimalFormat("#.###")
                                            df.roundingMode = RoundingMode.CEILING
                                            val hasilformat = df.format(hasilpersen)
                                            keyakinan.setText(hasilformat.toString() + "%")
                                            depresi.setText("Depresi Sedang")
                                            intentPenanganan("3", id_user)
                                        }
                                        else if(id_depresi == "4"){
                                            val hasilhitung = "${response.body()?.hasil_hitung}"
                                            val hsl = hasilhitung.toDouble()
                                            val hasilpersen = hsl * 100
                                            val df = DecimalFormat("#.###")
                                            df.roundingMode = RoundingMode.CEILING
                                            val hasilformat = df.format(hasilpersen)
                                            keyakinan.setText(hasilformat.toString() + "%")
                                            depresi.setText("Depresi Berat")
                                            intentPenanganan("4", id_user)
                                        }
                                        else{
                                            depresi.setText("-")
                                            keyakinan.setText("-")
                                            penanganan.visibility = View.GONE
                                        }
                                }
                            }

                            override fun onFailure(call: Call<getDeteksiModel>, t: Throwable) {
                                api.getLastDeteksi("" + id_user + "").enqueue(object : Callback<getDeteksiModel> {
                                    override fun onResponse(call: Call<getDeteksiModel>, response: Response<getDeteksiModel>) {
                                        if(response.isSuccessful){
                                            val id_depresi = "${response.body()?.tingkatdepresi_id}"
                                            if(id_depresi == "1"){
                                                val hasilhitung = "${response.body()?.hasil_hitung}"
                                                val hsl = hasilhitung.toDouble()
                                                val hasilpersen = hsl * 100
                                                val df = DecimalFormat("#.###")
                                                df.roundingMode = RoundingMode.CEILING
                                                val hasilformat = df.format(hasilpersen)
                                                keyakinan.setText(hasilformat.toString() + "%")
                                                depresi.setText("Tidak Depresi")
                                                penanganan.visibility = View.GONE

                                            }
                                            else if(id_depresi == "2"){
                                                val hasilhitung = "${response.body()?.hasil_hitung}"
                                                val hsl = hasilhitung.toDouble()
                                                val hasilpersen = hsl * 100
                                                val df = DecimalFormat("#.###")
                                                df.roundingMode = RoundingMode.CEILING
                                                val hasilformat = df.format(hasilpersen)
                                                keyakinan.setText(hasilformat.toString() + "%")
                                                depresi.setText("Depresi Ringan")
                                                intentPenanganan("2", id_user)
                                            }
                                            else if (id_depresi == "3"){
                                                val hasilhitung = "${response.body()?.hasil_hitung}"
                                                val hsl = hasilhitung.toDouble()
                                                val hasilpersen = hsl * 100
                                                val df = DecimalFormat("#.###")
                                                df.roundingMode = RoundingMode.CEILING
                                                val hasilformat = df.format(hasilpersen)
                                                keyakinan.setText(hasilformat.toString() + "%")
                                                depresi.setText("Depresi Sedang")
                                                intentPenanganan("3", id_user)
                                            }
                                            else if(id_depresi == "4"){
                                                val hasilhitung = "${response.body()?.hasil_hitung}"
                                                val hsl = hasilhitung.toDouble()
                                                val hasilpersen = hsl * 100
                                                val df = DecimalFormat("#.###")
                                                df.roundingMode = RoundingMode.CEILING
                                                val hasilformat = df.format(hasilpersen)
                                                keyakinan.setText(hasilformat.toString() + "%")
                                                depresi.setText("Depresi Berat")
                                                intentPenanganan("4", id_user)
                                            }
                                            else{
                                                depresi.setText("-")
                                                keyakinan.setText("-")
                                                penanganan.visibility = View.GONE
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<getDeteksiModel>, t: Throwable) {
                                    }

                                })
                            }

                        })

                intentKuesioner(id_user)
                intentProfil(id_user)
                intentArtikel(id_user)
                dialog.dismiss()
            }

            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getUser("" + idUser +"").enqueue(object : Callback<getUserModel> {
                        override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                            nama.setText("${response.body()?.nama}")
                            val gender = "${response.body()?.jenis_kelamin}"
                            val id_user = "${response.body()?.id}"
                            if(gender == "Perempuan"){
                                icon_gender.setImageResource(R.drawable.girl)
                            }else{
                                icon_gender.setImageResource(R.drawable.boy)
                            }
                            intentKuesioner(id_user)
                            intentProfil(id_user)
                            intentArtikel(id_user)
                            dialog.dismiss()
                        }

                        override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@HomeActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }

//            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
//                if(response.isSuccessful){
//                    val listdata = response.body()!!.user
//
//                    listdata.forEach{
//                        nama.setText("${it.nama}")
//                        val gender = "${it.jenis_kelamin}"
//                        val id_user = "${it.id}"
//                        if(gender == "Perempuan"){
//                            icon_gender.setImageResource(R.drawable.girl)
//                        }else{
//                            icon_gender.setImageResource(R.drawable.boy)
//                        }
//                        api.getLastDeteksi("" + id_user + "").enqueue(object : Callback<getDeteksiModel> {
//                            override fun onResponse(call: Call<getDeteksiModel>, response: Response<getDeteksiModel>) {
//                                if(response.isSuccessful){
//                                    val listdata = response.body()!!.deteksi
//
//                                    listdata.forEach{
//                                        val id_depresi = "${it.id_depresi}"
//                                        val hasilhitung = "${it.hasil_hitung}"
//                                        val hasil = hasilhitung.toDouble()
//                                        val hasilpersen = hasil * 100
//                                        keyakinan.setText(hasilpersen.toString() + "%")
//                                        if(id_depresi == "1"){
//                                            depresi.setText("Tidak Depresi")
//                                            penanganan.visibility = View.GONE
//
//                                        }
//                                        else if(id_depresi == "2"){
//                                            depresi.setText("Depresi Ringan")
//                                            intentPenanganan("2", id_user)
//                                        }
//                                        else if (id_depresi == "3"){
//                                            depresi.setText("Depresi Sedang")
//                                            intentPenanganan("3", id_user)
//                                        }
//                                        else if(id_depresi == "4"){
//                                            depresi.setText("Depresi Berat")
//                                            intentPenanganan("4", id_user)
//                                        }
//                                        else{
//                                            depresi.setText("-")
//                                            penanganan.visibility = View.GONE
//                                        }
//                                    }
//                                }
//                            }
//
//                            override fun onFailure(call: Call<getDeteksiModel>, t: Throwable) {
//                            }
//
//                        })
//
//                        btnArtikel = findViewById(R.id.btnArtikel)
//                        btnArtikel.setOnClickListener {
//                            val intent = Intent(this@HomeActivity, ArtikelActivity::class.java)
//                            intent.putExtra("id_user", id_user)
//                            startActivity(intent)
//                        }
//
//                        intentKuesioner(id_user)
//                        intentProfil(id_user)
//                    }
//                }
//                else{
//                    val builder = AlertDialog.Builder(this@HomeActivity)
//                    builder.setMessage(response.code().toString())
//                            .setPositiveButton("else", {
//                                dialogInterface, i ->
//                            })
//                            .setNegativeButton("TIDAK", {
//                                dialogInterface, i ->
//                            })
//                    val dialog = builder.create()
//                    dialog.show()
//                }
//            }
//
//            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
//                Log.e("HomeActivity", t.toString())
//            }


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

    private fun Double.roundDecimal(digit: Int) = "%.${digit}f".format(this)

    private fun intentProfil(id_user: String){
        btnProfil = findViewById(R.id.btnProfil)
        btnProfil.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

    private fun intentArtikel(id_user: String){
        btnArtikel = findViewById(R.id.btnArtikel)
        btnArtikel.setOnClickListener {
            val intent = Intent(this@HomeActivity, ArtikelActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
        }
    }

}