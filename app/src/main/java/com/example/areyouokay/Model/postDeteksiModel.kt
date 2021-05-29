package com.example.areyouokay.Model

data class postDeteksiModel(
        val id: Int,
        val pengguna: String,
        val createdAt: String?,
        val hasil_hitung: String?,
        val tingkatdepresi: String?
)