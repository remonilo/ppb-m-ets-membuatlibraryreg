package com.remonilo.libraryreg.data

data class Anggota(
    val id: Int,
    val nama: String,
    val nomorInduk: String,
    val programStudi: String,
    val alamat: String,
    val telepon: String,
    val email: String,
    val jenisKeanggotaan: String,
    val tanggalDaftar: String,
)
