package com.example.areyouokay.Model

data class getDeteksiModel(
        val id: Int,
        val pengguna_id: String,
        val hasil_hitung: String?,
        val createdAt: String?,
        val tingkatdepresi_id: String?
        )