package com.example.areyouokay.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.Model.postUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class EditProfilActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var editNama : TextView
    private lateinit var editTanggal : TextView
    private lateinit var editGender : RadioGroup
    private lateinit var editPekerjaan : RadioGroup
    private lateinit var edit : Button
    private lateinit var rj : RadioButton
    private lateinit var rg : RadioButton
    var cal = Calendar.getInstance()
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        val builder = AlertDialog.Builder(this@EditProfilActivity)
        inflater = this@EditProfilActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        val idUser = intent.getStringExtra("id_user")
        editNama = findViewById(R.id.editNama)
        editTanggal = findViewById(R.id.editTanggal)
        editGender = findViewById(R.id.editGender)
        editPekerjaan = findViewById(R.id.editPekerjaan)
        edit = findViewById(R.id.edit)

        val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    updateDateInView()
                }

        editTanggal.setOnClickListener {
            val date = DatePickerDialog(
                    this@EditProfilActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            )

            date.datePicker.maxDate = System.currentTimeMillis() - 1000

            date.show()

        }



        api.getUser("" + idUser +"").enqueue(object : Callback<getUserModel> {
            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                editNama.setText("${response.body()?.nama}")
                editTanggal.setText("${response.body()?.ttl}")
                val pekerjaan = "${response.body()?.pekerjaan}"
                val gender = "${response.body()?.jenis_kelamin}"
                val email = "${response.body()?.email}"
                if(gender == "Perempuan"){
                    editGender.check(R.id.editGenderP)
                }else{
                    editGender.check(R.id.editGenderL)
                }

                if(pekerjaan == "Pelajar/Mahasiswa dan Kerja"){
                    editPekerjaan.check(R.id.editMahasiswaKerja)
                }
                else if (pekerjaan == "Kerja"){
                    editPekerjaan.check(R.id.editKerja)
                }
                else if(pekerjaan == "Pelajar/Mahasiswa"){
                    editPekerjaan.check(R.id.editMahasiswa)
                }
                else{
                    editPekerjaan.check(R.id.editTidakKerja)
                }
                dialog.dismiss()

                edit.setOnClickListener{
                    val builder = AlertDialog.Builder(this@EditProfilActivity)
                    inflater = this@EditProfilActivity.layoutInflater

                    builder.setView(inflater.inflate(R.layout.loading_dialog, null))
                    builder.setCancelable(false)

                    dialog = builder.create()
                    dialog.show()

                    val nama = editNama.text
                    val ttl = editTanggal.text
                    rj = findViewById(editPekerjaan.getCheckedRadioButtonId())
                    rg = findViewById(editGender.getCheckedRadioButtonId())
                    val job = rj.text.toString()
                    val jenis_kelamin = rg.text.toString()
                    val tgl = LocalDate.parse(ttl)
                    val today = LocalDate.now()
                    var diff = Period.between(tgl, today)
                    api.updateUser(
                            "" + idUser + "",
                            ""  + nama + "",
                            "" + email + "",
                            "" + ttl + "",
                            "" + jenis_kelamin + "",
                            "" + job + "", "" + diff.years.toString() + ""
                    ).enqueue(object : Callback<postUserModel>{
                        override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                            if(response.isSuccessful) {
                                intentProfil(idUser)
                            }
                        }

                        override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                api.updateUser(
                                        "" + idUser + "",
                                        ""  + nama + "",
                                        "" + email + "",
                                        "" + ttl + "",
                                        "" + jenis_kelamin + "",
                                        "" + job + "", "" + diff.years.toString() + ""
                                ).enqueue(object : Callback<postUserModel>{
                                    override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                                        if(response.isSuccessful) {
                                            intentProfil(idUser)
                                        }
                                    }

                                    override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                        if(t is SocketTimeoutException){
                                            dialog.dismiss()
                                            Toast.makeText(this@EditProfilActivity,"timeout", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                })
                            }
                        }

                    })
                }
            }

            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getUser("" + idUser +"").enqueue(object : Callback<getUserModel> {
                        override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                            editNama.setText("${response.body()?.nama}")
                            editTanggal.setText("${response.body()?.ttl}")
                            val pekerjaan = "${response.body()?.pekerjaan}"
                            val gender = "${response.body()?.jenis_kelamin}"
                            val email = "${response.body()?.email}"
                            if(gender == "Perempuan"){
                                editGender.check(R.id.editGenderP)
                            }else{
                                editGender.check(R.id.editGenderL)
                            }

                            if(pekerjaan == "Mahasiswa dan Kerja"){
                                editPekerjaan.check(R.id.editMahasiswaKerja)
                            }
                            else if (pekerjaan == "Kerja"){
                                editPekerjaan.check(R.id.editKerja)
                            }
                            else {
                                editPekerjaan.check(R.id.editMahasiswa)
                            }
                            dialog.dismiss()

                            edit.setOnClickListener{
                                val builder = AlertDialog.Builder(this@EditProfilActivity)
                                inflater = this@EditProfilActivity.layoutInflater

                                builder.setView(inflater.inflate(R.layout.loading_dialog, null))
                                builder.setCancelable(false)

                                dialog = builder.create()
                                dialog.show()

                                val nama = editNama.text
                                val ttl = editTanggal.text
                                rj = findViewById(editPekerjaan.getCheckedRadioButtonId())
                                rg = findViewById(editGender.getCheckedRadioButtonId())
                                val job = rj.text.toString()
                                val jenis_kelamin = rg.text.toString()
                                val tgl = LocalDate.parse(ttl)
                                val today = LocalDate.now()
                                var diff = Period.between(tgl, today)
                                api.updateUser(
                                        "" + idUser + "",
                                        ""  + nama + "",
                                        "" + email + "",
                                        "" + ttl + "",
                                        "" + jenis_kelamin + "",
                                        "" + job + "", "" + diff.years.toString() + ""
                                ).enqueue(object : Callback<postUserModel>{
                                    override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                                        if(response.isSuccessful) {
                                            intentProfil(idUser)
                                        }
                                    }

                                    override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                        if(t is SocketTimeoutException){
                                            api.updateUser(
                                                    "" + idUser + "",
                                                    ""  + nama + "",
                                                    "" + email + "",
                                                    "" + ttl + "",
                                                    "" + jenis_kelamin + "",
                                                    "" + job + "", "" + diff.years.toString() + ""
                                            ).enqueue(object : Callback<postUserModel>{
                                                override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                                                    if(response.isSuccessful) {
                                                        intentProfil(idUser)
                                                    }
                                                }

                                                override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                                                    if(t is SocketTimeoutException){
                                                        dialog.dismiss()
                                                        Toast.makeText(this@EditProfilActivity,"timeout", Toast.LENGTH_LONG).show()
                                                    }
                                                }

                                            })
                                        }
                                    }

                                })
                            }
                        }

                        override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@EditProfilActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        })


    }

    private fun intentProfil(id_user: String){
            val intent = Intent(this, ProfilActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editTanggal.setText(sdf.format(cal.time))
    }
}