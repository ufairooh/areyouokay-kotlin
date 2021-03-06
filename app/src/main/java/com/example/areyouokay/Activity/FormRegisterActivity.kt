package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.postUserModel
import com.example.areyouokay.R
import okhttp3.internal.http.toHttpDateOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class FormRegisterActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    private val api by lazy { ApiRetrofit().endpoint }

    private lateinit var dialogAlert: DialogInterface
    private lateinit var btnSelesai: Button
    private lateinit var Selesai: Button
    private lateinit var btn_prev: Button
    private lateinit var btn_next: Button
    private lateinit var pilihTanggal: EditText
    private lateinit var inputNama: EditText
    private lateinit var rg_gender: RadioGroup
    private lateinit var rg_job: RadioGroup
    private lateinit var frame_nama: FrameLayout
    private lateinit var frame_gender: FrameLayout
    private lateinit var frame_age: FrameLayout
    private lateinit var frame_job: FrameLayout
    private lateinit var txt_indicator: TextView
    private lateinit var rg: RadioButton
    private lateinit var rj: RadioButton
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_register)

        handlingUI()



        btnSelesai = findViewById(R.id.btnSelesai)
        rg_job = findViewById(R.id.rg_job)
        pilihTanggal = findViewById(R.id.pilihTanggal)
        inputNama = findViewById(R.id.inputNama)

        val email = intent.getStringExtra("email")

        btnSelesai.setOnClickListener {
            if(rg_job.getCheckedRadioButtonId() != -1){
                //createUser()
                rj = findViewById(rg_job.getCheckedRadioButtonId())
                rg = findViewById(rg_gender.getCheckedRadioButtonId())
                val rs_job = rj.text.toString()
                val rs_gender = rg.text.toString()
                val rs_tglLahir = pilihTanggal.getText().toString()
                val rs_nama = inputNama.text.toString()
                val tgl = LocalDate.parse(rs_tglLahir)
                val today = LocalDate.now()
                var diff = Period.between(tgl, today)

                val builder = AlertDialog.Builder(this@FormRegisterActivity)
                builder.setMessage("Apakah data yang kamu isikan sudah benar?")
                        .setPositiveButton("IYA", {
                            dialogInterface, i ->
                            val builder = AlertDialog.Builder(this@FormRegisterActivity)
                            inflater = this@FormRegisterActivity.layoutInflater

                            builder.setView(inflater.inflate(R.layout.loading_dialog, null))
                            builder.setCancelable(false)

                            dialog = builder.create()
                            dialog.show()
                            api.createUser(
                                    "" + rs_nama + "",
                                    ""  + email + "",
                                    "" + rs_tglLahir + "",
                                    "" + rs_gender + "",
                                    "" + rs_job + "", "" + diff.years.toString() + ""
                            ).enqueue(object : Callback<postUserModel>{
                                override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                                    if(response.isSuccessful){
                                        val iduser = "${response.body()?.id}"
                                        intent(iduser);
                                    }else{
                                        val builder = AlertDialog.Builder(this@FormRegisterActivity)
                                        builder.setMessage(response.code().toString())
                                                .setPositiveButton("else", {
                                                    dialogInterface, i ->
                                                })
                                                .setNegativeButton("TIDAK", {
                                                    dialogInterface, i ->
                                                })
                                        val dialog = builder.create()
                                        dialog.show()
                                    }

                                }

                                override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                    if(t is SocketTimeoutException){
                                        api.createUser(
                                                "" + rs_nama + "",
                                                ""  + email + "",
                                                "" + rs_tglLahir + "",
                                                "" + rs_gender + "",
                                                "" + rs_job + "", "" + diff.years.toString() + ""
                                        ).enqueue(object : Callback<postUserModel>{
                                            override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                                                if(response.isSuccessful){
                                                    val iduser = "${response.body()?.id}"
                                                    intent(iduser);
                                                }else{
                                                    val builder = AlertDialog.Builder(this@FormRegisterActivity)
                                                    builder.setMessage(response.code().toString())
                                                            .setPositiveButton("else", {
                                                                dialogInterface, i ->
                                                            })
                                                            .setNegativeButton("TIDAK", {
                                                                dialogInterface, i ->
                                                            })
                                                    val dialog = builder.create()
                                                    dialog.show()
                                                }

                                            }

                                            override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                                if(t is SocketTimeoutException){
                                                    dialog.dismiss()
                                                    Toast.makeText(this@FormRegisterActivity,"timeout", Toast.LENGTH_LONG).show()
                                                }
                                            }

                                        })
                                    }
                                }

                            })
                        })
                        .setNegativeButton("TIDAK", {
                            dialogInterface, i ->
                        })
                val dialog = builder.create()
                dialog.show()


            }else{
                Toast.makeText(this,"Jawaban tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
        }

        pilihTanggal = findViewById(R.id.pilihTanggal)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView()
            }

        pilihTanggal.setOnClickListener {
            val date = DatePickerDialog(
                this@FormRegisterActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            date.datePicker.maxDate = System.currentTimeMillis() - 1000

            date.show()

        }
    }

    private fun intent(id: String){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("id_user", id)
        startActivity(intent)
        finish()
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        pilihTanggal.setText(sdf.format(cal.time))
    }

    @SuppressLint("SetTextI18n")
    private fun handlingUI() {

        var page = 0

        frame_nama = findViewById(R.id.frame_nama)
        frame_gender = findViewById(R.id.frame_gender)
        frame_age = findViewById(R.id.frame_age)
        frame_job = findViewById(R.id.frame_job)

        val frameList = arrayOf(
                frame_nama,
                frame_gender,
                frame_age,
                frame_job
        )

        btn_prev = findViewById(R.id.btn_prev)
        btn_prev.visibility = View.INVISIBLE

        btn_next = findViewById(R.id.btn_next)
        btn_next.setOnClickListener {
            val pass = pageLayoutChecking(page)

            when {
                // change page
                pass -> {

                    // change page
                    page++
                    btn_prev.visibility = View.VISIBLE
                    when(page){
                        frameList.size - 1 -> btn_next.visibility = View.GONE
                    }
                    frameList.forEach { i ->
                        i.visibility = View.GONE
                    }
                    frameList[page].visibility = View.VISIBLE
                }

                else -> {
                    // alert
                    Toast.makeText(this,"Jawaban tidak boleh kosong", Toast.LENGTH_LONG).show()
                }
            }


            btn_prev.setOnClickListener {
                page--

                btn_next.visibility = View.VISIBLE
                when (page) {
                    0 -> btn_prev.visibility = View.INVISIBLE
                }
                frameList.forEach { i ->
                    i.visibility = View.GONE
                }
                frameList[page].visibility = View.VISIBLE
            }
        }
    }

    private fun pageLayoutChecking(index: Int): Boolean {
        inputNama = findViewById(R.id.inputNama)
        rg_gender = findViewById(R.id.rg_gender)
        pilihTanggal = findViewById(R.id.pilihTanggal)
        rg_job = findViewById(R.id.rg_job)
        return when (index) {
            0 -> inputNama.getText().toString() != ""
            1 -> rg_gender.getCheckedRadioButtonId() != -1
            2 -> pilihTanggal.getText().toString() != ""
            3 -> rg_job.getCheckedRadioButtonId() != -1
            else -> false

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@FormRegisterActivity)
        builder.setMessage("Apakah kamu yakin ingin keluar?")
                .setPositiveButton("IYA", {
                    dialogInterface, i -> finishAffinity()
                })
                .setNegativeButton("TIDAK", {
                    dialogInterface, i ->
                })
        val dialog = builder.create()
        dialog.show()
    }

}