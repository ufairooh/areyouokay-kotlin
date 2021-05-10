package com.example.areyouokay.Model

data class getDeteksiModel(
        val deteksi: List<Data>
){
    data class Data(
            val id_user: String,
            val tanggal: String?,
            val hasil_hitung: String?,
            val id_depresi: String?
    )
}