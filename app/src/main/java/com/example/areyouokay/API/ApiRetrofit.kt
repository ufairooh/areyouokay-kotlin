package com.example.areyouokay.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiRetrofit {

    val endpoint: ApiEndpoint
    get(){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://are-you-okay.herokuapp.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(ApiEndpoint::class.java)
    }

}