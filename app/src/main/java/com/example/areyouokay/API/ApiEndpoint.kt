package com.example.areyouokay.API

import com.example.areyouokay.Model.*
import retrofit2.Call
import retrofit2.http.*
import java.sql.Timestamp

interface ApiEndpoint {

    @Headers("Accept: application/json")
    @GET("pengguna-detail/{id}")
    fun getUser(
            @Path("id") id: String
    ) : Call<getUserModel>

    @Headers("Accept: application/json")
    @GET("pengguna-detail-byemail/{email}")
    fun getUserByEmail(
            @Path("email") email: String
    ) : Call<getUserModel>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("pengguna-create/")
    fun createUser(
            @Field("nama") nama: String,
            @Field("email") email: String,
            @Field("ttl") ttl: String,
            @Field("jenis_kelamin") jenis_kelamin: String,
            @Field("pekerjaan") pekerjaan: String
    ): Call<postUserModel>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("pengguna-update/{id}/                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ")
    fun updateUser(
            @Path("id") id: String,
            @Field("nama") nama: String,
            @Field("email") email: String,
            @Field("ttl") ttl: String,
            @Field("jenis_kelamin") jenis_kelamin: String,
            @Field("pekerjaan") pekerjaan: String
    ): Call<postUserModel>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("hasildeteksi-create/")
    fun createHasilDeteksi(
            @Field("pengguna_id") pengguna_id: String,
            @Field("createdAt") createdAt: String,
            @Field("hasil_hitung") hasil_hitung: String,
            @Field("tingkatdepresi_id") tingkatdepresi_id: String
    ): Call<postDeteksiModel>

    @Headers("Accept: application/json")
    @GET("hasildeteksi-detail-by-idpengguna/{pengguna_id}/")
    fun getDeteksi(
            @Path("pengguna_id") pengguna_id: String
    ) : Call<List<getDeteksiModel>>

    @Headers("Accept: application/json")
    @GET("hasildeteksi-detail-by-recentidpengguna/{pengguna_id}/")
    fun getLastDeteksi(
            @Path("pengguna_id") pengguna_id: String
    ) : Call<getDeteksiModel>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("historypertanyaanjawaban-create/")
    fun createPertanyaanJawaban(
            @Field("hasildeteksi_id") hasildeteksi_id: String,
            @Field("pertanyaan_id") pertanyaan_id: String,
            @Field("jawaban_id") jawaban_id: String
    ): Call<postPertanyaanJawabanModel>

    @Headers("Accept: application/json")
    @GET("penanganan-detail-by-idtingkatdepresi/{tingkatdepresi_id}/")
    fun gePenanganan(
            @Path("tingkatdepresi_id") tingkatdepresi_id: String
    ) : Call<getPenangananModel>

    @GET("artikel-list/")
    fun getArtikel(
    ) : Call<List<getArtikelModel>>

    @Headers("Accept: application/json")
    @GET("artikel-detail/{id}/")
    fun getIsiArtikel(
            @Path("id") id: String
    ) : Call<getArtikelModel>
    
}