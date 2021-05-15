package com.example.areyouokay.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Adapter.HistoryAdapter
import com.example.areyouokay.LoginActivity
import com.example.areyouokay.Model.getDeteksiModel
import com.example.areyouokay.Model.getUserModel
import com.example.areyouokay.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ProfilActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var namaUser: TextView
    private lateinit var ttlUser: TextView
    private lateinit var pekerjaanUser: TextView
    private lateinit var genderUser: ImageView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var btnEdit: ImageView
    private lateinit var logout:  TextView
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        val builder = AlertDialog.Builder(this@ProfilActivity)
        inflater = this@ProfilActivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        namaUser = findViewById(R.id.namaUser)
        ttlUser = findViewById(R.id.ttlUser)
        pekerjaanUser = findViewById(R.id.pekerjaanUser)
        genderUser = findViewById(R.id.genderUser)

        val idUser = intent.getStringExtra("id_user")

        api.getUser("" + idUser +"").enqueue(object : Callback<getUserModel> {
            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                namaUser.setText("${response.body()?.nama}")
                ttlUser.setText("${response.body()?.ttl}")
                pekerjaanUser.setText("${response.body()?.pekerjaan}")
                val gender = "${response.body()?.jenis_kelamin}"
                val id_user = "${response.body()?.id}"
                if(gender == "Perempuan"){
                    genderUser.setImageResource(R.drawable.girl)
                }else{
                    genderUser.setImageResource(R.drawable.boy)
                }
                //dialog.dismiss()
            }

            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getUser("" + idUser +"").enqueue(object : Callback<getUserModel> {
                        override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                            namaUser.setText("${response.body()?.nama}")
                            ttlUser.setText("${response.body()?.ttl}")
                            pekerjaanUser.setText("${response.body()?.pekerjaan}")
                            val gender = "${response.body()?.jenis_kelamin}"
                            val id_user = "${response.body()?.id}"
                            if(gender == "Perempuan"){
                                genderUser.setImageResource(R.drawable.girl)
                            }else{
                                genderUser.setImageResource(R.drawable.boy)
                            }
                            //dialog.dismiss()
                        }

                        override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@ProfilActivity,"timeout", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        })

        setupList()

        btnEdit = findViewById(R.id.btnEdit)
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditProfilActivity::class.java)
            intent.putExtra("id_user", idUser)
            startActivity(intent)
        }

        logout = findViewById(R.id.logout)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        logout.setOnClickListener {
            mGoogleSignInClient.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
}

    override fun onStart() {
        super.onStart()
        val idUser = intent.getStringExtra("id_user")
        getHistory(idUser)
    }

    private fun setupList(){
        recyclerHistory = findViewById(R.id.recycler)
        historyAdapter = HistoryAdapter(arrayListOf())
        recyclerHistory.adapter = historyAdapter
    }

    private fun getHistory(id_user: String){
        api.getDeteksi(id_user).enqueue(object :
                Callback<List<getDeteksiModel>> {
            override fun onResponse(call: Call<List<getDeteksiModel>>, response: Response<List<getDeteksiModel>>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!
                    historyAdapter.setData(listData)
                    dialog.dismiss()

                }
            }

            override fun onFailure(call: Call<List<getDeteksiModel>>, t: Throwable) {
                if(t is SocketTimeoutException){
                    api.getDeteksi(id_user).enqueue(object :
                            Callback<List<getDeteksiModel>> {
                        override fun onResponse(call: Call<List<getDeteksiModel>>, response: Response<List<getDeteksiModel>>) {
                            if (response.isSuccessful) {
                                val listData = response.body()!!
                                historyAdapter.setData(listData)
                                dialog.dismiss()

                            }
                        }

                        override fun onFailure(call: Call<List<getDeteksiModel>>, t: Throwable) {
                            if(t is SocketTimeoutException){
                                dialog.dismiss()
                                Toast.makeText(this@ProfilActivity,"timeout", Toast.LENGTH_LONG).show()
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