package com.example.areyouokay.Model

data class postDeteksiModel(
        val id_user: String,
        val tanggal: String?,
        val hasil_hitung: String?,
        val id_depresi: String?,
        val id_deteksi: List<Data>
){
    data class Data(
            val id_deteksi: Int
    )
}