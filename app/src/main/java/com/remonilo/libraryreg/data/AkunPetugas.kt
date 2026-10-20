package com.remonilo.libraryreg.data

// Akun petugas ditulis langsung di kode sebagai asumsi; belum ada autentikasi ke server.
object AkunPetugas {

    private val akun = mapOf(
        "petugas1" to "perpus123",
        "petugas2" to "perpus456",
    )

    var sedangMasuk = false
        private set

    fun masuk(username: String, password: String): Boolean {
        sedangMasuk = akun[username.trim()] == password
        return sedangMasuk
    }

    fun keluar() {
        sedangMasuk = false
    }
}
