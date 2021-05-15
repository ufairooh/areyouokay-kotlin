package com.example.areyouokay

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.areyouokay.API.ApiRetrofit
import com.example.areyouokay.Activity.FormRegisterActivity
import com.example.areyouokay.Activity.HomeActivity
import com.example.areyouokay.Model.getUserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.util.CollectionUtils.isEmpty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var loginViewPager: ViewPager2
    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var buttonLogin: SignInButton
    private lateinit var test: TextView
    private var sliderHandler = Handler()
    private lateinit var dialog: AlertDialog
    private lateinit var inflater: LayoutInflater

    private val loginSliderAdapter = LoginSliderAdapter(
            listOf(
                    LoginSlider(
                            "Are You Okay",
                            "Are You Okay merupakan aplikasi yang dapat melakukan deteksi dini terkait depresi",
                            R.drawable.logo3
                    ),
                    LoginSlider(
                            "Deteksi Dini Depresi",
                            "lakukan deteksi dini terkait depresi agar kamu bisa mendapatkan penanganan yang tepat",
                            R.drawable.deteksi3
                    ),
                    LoginSlider(
                            "baca artikel terkait depresi",
                            "tambah wawasan kamu terkait depresi dan hal hal yang dapat membantu kamu dalam mengatasi depresi",
                            R.drawable.artikel3
                    )
            )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewPager = findViewById(R.id.loginViewPager)
        loginViewPager.adapter = loginSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        loginViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                sliderHandler.postDelayed(object : Runnable{
                    override fun run(){
                       loginViewPager.setCurrentItem(loginViewPager.currentItem + 1)
                    }
                }, 6000)
            }
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonLogin.setOnClickListener(){
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if(acct != null){
            val builder = AlertDialog.Builder(this@LoginActivity)
            inflater = this@LoginActivity.layoutInflater

            builder.setView(inflater.inflate(R.layout.loading_dialog, null))
            builder.setCancelable(false)

            dialog = builder.create()
            dialog.show()
            val personName = acct.displayName
            val personEmail = acct.email
            val personId = acct.id

            api.getUserByEmail("" + personEmail +"").enqueue(object : Callback<getUserModel> {

                override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                    if(response.isSuccessful){
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("id_user", "${response.body()?.id}")
                        startActivity(intent)
                    }
                    else{
                        val intent = Intent(this@LoginActivity, FormRegisterActivity::class.java)
                        intent.putExtra("nama", personName)
                        intent.putExtra("email", personEmail)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                    if(t is SocketTimeoutException){
                        api.getUserByEmail("" + personEmail +"").enqueue(object : Callback<getUserModel> {

                            override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                                if(response.isSuccessful){
                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    intent.putExtra("id_user", "${response.body()?.id}")
                                    startActivity(intent)
                                }
                                else{
                                    val intent = Intent(this@LoginActivity, FormRegisterActivity::class.java)
                                    intent.putExtra("nama", personName)
                                    intent.putExtra("email", personEmail)
                                    startActivity(intent)
                                }
                            }

                            override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                                if(t is SocketTimeoutException){
                                    dialog.dismiss()
                                    Toast.makeText(this@LoginActivity,"timeout", Toast.LENGTH_LONG).show()
                                }
                            }

                        })
                    }
                }

            })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)

                val builder = AlertDialog.Builder(this@LoginActivity)
                inflater = this@LoginActivity.layoutInflater

                builder.setView(inflater.inflate(R.layout.loading_dialog, null))
                builder.setCancelable(false)

                dialog = builder.create()
                dialog.show()

                api.getUserByEmail("" + account!!.email +"").enqueue(object : Callback<getUserModel> {

                    override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                        if(response.isSuccessful){
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                intent.putExtra("id_user", "${response.body()?.id}")
                                startActivity(intent)
                        }
                        else{
                            val intent = Intent(this@LoginActivity, FormRegisterActivity::class.java)
                            intent.putExtra("nama", account!!.displayName)
                            intent.putExtra("email", account!!.email)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                        if(t is SocketTimeoutException){
                            api.getUserByEmail("" + account!!.email +"").enqueue(object : Callback<getUserModel> {

                                override fun onResponse(call: Call<getUserModel>, response: Response<getUserModel>) {
                                    if(response.isSuccessful){
                                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                        intent.putExtra("id_user", "${response.body()?.id}")
                                        startActivity(intent)
                                    }
                                    else{
                                        val intent = Intent(this@LoginActivity, FormRegisterActivity::class.java)
                                        intent.putExtra("nama", account!!.displayName)
                                        intent.putExtra("email", account!!.email)
                                        startActivity(intent)
                                    }
                                }

                                override fun onFailure(call: Call<getUserModel>, t: Throwable) {
                                    if(t is SocketTimeoutException){
                                        dialog.dismiss()
                                        Toast.makeText(this@LoginActivity,"timeout", Toast.LENGTH_LONG).show()
                                    }
                                }

                            })
                        }
                    }

                })
            } catch (e :ApiException){
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(loginSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                        ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.indicator_inactive
                        )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer = findViewById(R.id.indicatorsContainer)
            indicatorsContainer.addView(indicators[i])
        }
    }
    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorsContainer.childCount
        for(i in 0 until childCount){
            val imageView = indicatorsContainer[i] as ImageView
            if(i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.indicator_active
                        )
                )
            }
            else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.indicator_inactive
                        )
                )
            }
        }
    }
}