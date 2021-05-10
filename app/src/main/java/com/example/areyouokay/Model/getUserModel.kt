package com.example.areyouokay.Model

data class getUserModel(
        val user: List<Data>
){
    data class Data(
            val id_user: Int,
            val nama: String?,
            val email: String?,
            val ttl: String?,
            val jenis_kelamin: String?,
            val pekerjaan: String?
    )
}