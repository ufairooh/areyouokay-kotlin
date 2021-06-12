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
            @Field("pekerjaan") pekerjaan: String,
            @Field("umur") umur: String
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
            @Field("pekerjaan") pekerjaan: String,
            @Field("umur") umur: String
    ): Call<postUserModel>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("hasildeteksi-create/")
    fun createHasilDeteksi(
            @Field("pengguna") pengguna: String,
            @Field("createdAt") createdAt: String,
            @Field("hasil_hitung") hasil_hitung: String,
            @Field("tingkatdepresi") tingkatdepresi_id: String
    ): Call<postDeteksiModel>

    @Headers("Accept: application/json")
    @GET("hasildeteksi-detail-by-idpengguna/{pengguna}/")
    fun getDeteksi(
            @Path("pengguna") pengguna: String
    ) : Call<List<getDeteksiModel>>

    @Headers("Accept: application/json")
    @GET("hasildeteksi-detail-by-recentidpengguna/{pengguna}/")
    fun getLastDeteksi(
            @Path("pengguna") pengguna: String
    ) : Call<getDeteksiModel>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("historypertanyaanjawaban-create/")
    fun createPertanyaanJawaban(
            @Field("jawaban_1") jawaban_1: String,
            @Field("jawaban_2") jawaban_2: String,
            @Field("jawaban_3") jawaban_3: String,
            @Field("jawaban_4") jawaban_4: String,
            @Field("jawaban_5") jawaban_5: String,
            @Field("jawaban_6") jawaban_6: String,
            @Field("jawaban_7") jawaban_7: String,
            @Field("jawaban_8") jawaban_8: String,
            @Field("jawaban_9") jawaban_9: String,
            @Field("jawaban_10") jawaban_10: String,
            @Field("jawaban_11") jawaban_11: String,
            @Field("jawaban_12") jawaban_12: String,
            @Field("jawaban_13") jawaban_13: String,
            @Field("jawaban_14") jawaban_14: String,
            @Field("jawaban_15") jawaban_15: String,
            @Field("jawaban_16") jawaban_16: String,
            @Field("jawaban_17") jawaban_17: String,
            @Field("jawaban_18") jawaban_18: String,
            @Field("jawaban_19") jawaban_19: String,
            @Field("jawaban_20") jawaban_20: String,
            @Field("hasildeteksi") hasildeteksi: String
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