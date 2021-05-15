package com.example.areyouokay.Model

data class postDeteksiModel(
        val id: Int,
        val pengguna_id: String,
        val createdAt: String?,
        val hasil_hitung: String?,
        val tingkatdepresi_id: String?
)