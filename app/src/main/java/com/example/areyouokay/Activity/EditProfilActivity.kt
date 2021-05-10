package com.example.areyouokay.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.Model.postUserModel
import com.example.areyouokay.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfilActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var editNama : TextView
    private lateinit var editTanggal : TextView
    private lateinit var editGender : RadioGroup
    private lateinit var editPekerjaan : RadioGroup
    private lateinit var edit : Button
    private lateinit var rj : RadioButton
    private lateinit var rg : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        val idUser = intent.getStringExtra("id_user")
        editNama = findViewById(R.id.editNama)
        editTanggal = findViewById(R.id.editTanggal)
        editGender = findViewById(R.id.editGender)
        editPekerjaan = findViewById(R.id.editPekerjaan)
        edit = findViewById(R.id.edit)

        api.getUser("" ,"" , "" + idUser + "").enqueue(object :
            Callback<getUserModel> {

            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                if(response.isSuccessful){
                    val listdata = response.body()!!.user

                    listdata.forEach{
                        editNama.setText("${it.nama}")
                        editTanggal.setText("${it.ttl}")
                        val pekerjaan = "${it.pekerjaan}"
                        val gender = "${it.jenis_kelamin}"
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

                    }
                }
            }

            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
            }
        })

        edit.setOnClickListener{
            val nama = editNama.text
            val ttl = editTanggal.text
            rj = findViewById(editPekerjaan.getCheckedRadioButtonId())
            rg = findViewById(editGender.getCheckedRadioButtonId())
            val pekerjaan = rj.text.toString()
            val jenis_kelamin = rg.text.toString()
            api.updateUser(
                    "" + idUser + "",
                    ""  + nama + "",
                    "" + ttl + "",
                    "" + jenis_kelamin + "",
                    "" + pekerjaan + ""
            ).enqueue(object : Callback<postUserModel>{
                override fun onResponse(call: Call<postUserModel>, response: Response<postUserModel>) {
                    if(response.isSuccessful) {
                        intentProfil(idUser)
                    }
                }

                override fun onFailure(call: Call<postUserModel>, t: Throwable) {
                    Log.e("EditProfilActivity", t.toString())
                    intentProfil(idUser)
                }

            })
        }
    }

    private fun intentProfil(id_user: String){
            val intent = Intent(this, ProfilActivity::class.java)
            intent.putExtra("id_user", id_user)
            startActivity(intent)
    }
}