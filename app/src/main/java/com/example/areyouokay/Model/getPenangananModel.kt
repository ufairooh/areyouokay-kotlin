package com.example.areyouokay.Model

data class getPenangananModel(
        val penanganan: List<Data>
){
    data class Data(
            val id_depresi: Int,
            val judul: String?,
            val cover: String?,
            val isi: String?
    )
}