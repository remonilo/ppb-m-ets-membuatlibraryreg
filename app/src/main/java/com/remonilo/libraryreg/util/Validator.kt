package com.remonilo.libraryreg.util

import android.util.Patterns
import com.remonilo.libraryreg.data.AnggotaRepository

// Setiap fungsi mengembalikan pesan kesalahan, atau null jika input valid.
object Validator {

    const val PANJANG_NIM = 9
    const val PANJANG_NIP = 18

    fun wajib(nilai: String, label: String): String? =
        if (nilai.isBlank()) "$label wajib diisi" else null

    fun pilihan(nilai: String, label: String): String? =
        if (nilai.isBlank()) "$label wajib dipilih" else null

    fun nomorInduk(nilai: String, jenisKeanggotaan: String, kecualiId: Int?): String? {
        if (nilai.isBlank()) return "NIM/NIP wajib diisi"
        if (!nilai.all { it.isDigit() }) return "NIM/NIP hanya boleh berisi angka"

        // Panjang hanya dicek jika jenis keanggotaan sudah dipilih, karena aturannya bergantung pada jenis.
        if (jenisKeanggotaan.isNotBlank()) {
            val mahasiswa = jenisKeanggotaan == "Mahasiswa"
            val panjang = if (mahasiswa) PANJANG_NIM else PANJANG_NIP
            if (nilai.length != panjang) {
                return if (mahasiswa) "NIM mahasiswa harus $PANJANG_NIM digit" else "NIP harus $PANJANG_NIP digit"
            }
        }

        if (AnggotaRepository.nomorIndukTerpakai(nilai, kecualiId)) {
            return "NIM/NIP ini sudah terdaftar. Periksa kembali nomor induk."
        }
        return null
    }

    fun telepon(nilai: String): String? = when {
        nilai.isBlank() -> "Nomor telepon wajib diisi"
        !nilai.all { it.isDigit() } -> "Nomor telepon hanya boleh berisi angka"
        !nilai.startsWith("08") -> "Nomor telepon harus diawali 08"
        nilai.length !in 10..13 -> "Nomor telepon harus 10 sampai 13 digit"
        else -> null
    }

    fun email(nilai: String): String? = when {
        nilai.isBlank() -> "Email wajib diisi"
        !Patterns.EMAIL_ADDRESS.matcher(nilai).matches() -> "Format email tidak valid. Contoh: nama@kampus.ac.id"
        else -> null
    }
}
