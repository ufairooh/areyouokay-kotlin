package com.example.areyouokay.Model

data class getArtikelModel(
        val artikel: List<Data>
){
    data class Data(
            val id_artikel: String,
            val judul: String?,
            val cover: String?,
            val isi: String?
    )
}