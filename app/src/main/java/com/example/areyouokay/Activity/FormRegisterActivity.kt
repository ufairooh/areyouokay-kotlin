package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.postUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FormRegisterActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    private val api by lazy { ApiRetrofit().endpoint }

    private lateinit var dialogAlert: DialogInterface
    private lateinit var btnSelesai: Button
    private lateinit var btn_prev: Button
    private lateinit var btn_next: Button
    private lateinit var pilihTanggal: EditText
    private lateinit var rg_gender: RadioGroup
    private lateinit var rg_job: RadioGroup
    private lateinit var frame_gender: FrameLayout
    private lateinit var frame_age: FrameLayout
    private lateinit var frame_job: FrameLayout
    private lateinit var txt_indicator: TextView
    private lateinit var rg: RadioButton
    private lateinit var rj: RadioButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_register)

        handlingUI()



        btnSelesai = findViewById(R.id.btnSelesai)
        rg_job = findViewById(R.id.rg_job)
        pilihTanggal = findViewById(R.id.pilihTanggal)

        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")

        btnSelesai.setOnClickListener {
            if(rg_job.getCheckedRadioButtonId() != -1){
                //createUser()
                rj = findViewById(rg_job.getCheckedRadioButtonId())
                rg = findViewById(rg_gender.getCheckedRadioButtonId())
                val rs_job = rj.text.toString()
                val rs_gender = rg.text.toString()
                val rs_tglLahir = pilihTanggal.getText().toString()

                val builder = AlertDialog.Builder(this@FormRegisterActivity)
                builder.setMessage("Apakah data yang kamu isikan sudah benar?")
                        .setPositiveButton("IYA", {
                            dialogInterface, i ->
                            api.createUser(
                                    "" + nama + "",
                                    ""  + email + "",
                                    "" + rs_tglLahir + "",
                                    "" + rs_gender + "",
                                    "" + rs_job + ""
                            ).enqueue(object : Callback<postUserModel>{
                                override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                                    intent();
                                }

                                override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                    Log.e("FormRegisterActivity", t.toString())
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

    private fun intent(){
        val namaUser = intent.getStringExtra("nama")
        val emailUser = intent.getStringExtra("email")
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("nama", namaUser)
        intent.putExtra("email", emailUser)
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

        frame_gender = findViewById(R.id.frame_gender)
        frame_age = findViewById(R.id.frame_age)
        frame_job = findViewById(R.id.frame_job)

        val frameList = arrayOf(
            frame_gender,
            frame_age,
            frame_job
        )

        btn_prev = findViewById(R.id.btn_prev)
        btn_prev.visibility = View.INVISIBLE

        txt_indicator = findViewById(R.id.txt_indicator)
        txt_indicator.text = (page + 1).toString() + " / " + frameList.size

        btn_next = findViewById(R.id.btn_next)
        btn_next.setOnClickListener {
            val pass = pageLayoutChecking(page)

            when {
                // change page
                pass -> {

                    // change page
                    page++
                    txt_indicator.text = (page + 1).toString() + " / " + frameList.size
                    btn_prev.visibility = View.VISIBLE
                    when (page) {
                        frameList.size - 1 -> btn_next.visibility = View.INVISIBLE

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
                txt_indicator.text = (page+1).toString() + " / " + frameList.size

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
        rg_gender = findViewById(R.id.rg_gender)
        pilihTanggal = findViewById(R.id.pilihTanggal)
        rg_job = findViewById(R.id.rg_job)
        return when (index) {
            0 -> rg_gender.getCheckedRadioButtonId() != -1
            1 -> pilihTanggal.getText().toString() != ""
            2 -> rg_job.getCheckedRadioButtonId() != -1
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