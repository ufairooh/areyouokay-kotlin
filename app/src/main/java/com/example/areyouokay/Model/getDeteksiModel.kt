package com.example.areyouokay.Model

data class getDeteksiModel(
        val id: Int,
        val pengguna: String,
        val createdAt: String?,
        val hasil_hitung: String?,
        val tingkatdepresi: String?
        )