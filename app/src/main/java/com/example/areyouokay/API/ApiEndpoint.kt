package com.example.areyouokay.API

import com.example.areyouokay.Model.*
import retrofit2.Call
import retrofit2.http.*
import java.sql.Timestamp

interface ApiEndpoint {

    @GET("readUser.php")
    fun getUser(
            @Query("email") email: String,
            @Query("nama") nama: String,
            @Query("id_user") id_user: String
    ) : Call<getUserModel>

    @FormUrlEncoded
    @POST("createUser.php")
    fun createUser(
            @Field("nama") nama: String,
            @Field("email") email: String,
            @Field("ttl") ttl: String,
            @Field("jenis_kelamin") jenis_kelamin: String,
            @Field("pekerjaan") pekerjaan: String
    ): Call<postUserModel>

    @FormUrlEncoded
    @PUT("updateUser.php")
    fun updateUser(
            @Field("id_user") id_user: String,
            @Field("nama") nama: String,
            @Field("ttl") ttl: String,
            @Field("jenis_kelamin") jenis_kelamin: String,
            @Field("pekerjaan") pekerjaan: String
    ): Call<postUserModel>

    @FormUrlEncoded
    @POST("createHasilDeteksi.php")
    fun createHasilDeteksi(
            @Field("id_user") id_user: String,
            @Field("tanggal") tanggal: String,
            @Field("hasil_hitung") hasil_hitung: String,
            @Field("id_depresi") id_depresi: String
    ): Call<postDeteksiModel>

    @GET("readHasilDeteksi.php")
    fun getDeteksi(
            @Query("id_user") id_user: String
    ) : Call<getDeteksiModel>

    @GET("readLastDeteksi.php")
    fun getLastDeteksi(
            @Query("id_user") id_user: String
    ) : Call<getDeteksiModel>

    @FormUrlEncoded
    @POST("createPertanyaanJawaban.php")
    fun createPertanyaanJawaban(
            @Field("id_deteksi") id_deteksi: String,
            @Field("id_pertanyaan") id_pertanyaan: String,
            @Field("id_jawaban") id_jawaban: String
    ): Call<postPertanyaanJawabanModel>

    @GET("readPenanganan.php")
    fun gePenanganan(
            @Query("id_depresi") id_depresi: String
    ) : Call<getPenangananModel>

    @GET("readArtikel.php")
    fun getArtikel(
    ) : Call<getArtikelModel>

    @GET("readIsiArtikel.php")
    fun getIsiArtikel(
            @Query("id_artikel") id_artikel: String
    ) : Call<getArtikelModel>
    
}