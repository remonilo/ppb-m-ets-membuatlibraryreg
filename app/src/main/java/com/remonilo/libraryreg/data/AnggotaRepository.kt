package com.remonilo.libraryreg.data

// Penyimpanan in-memory sesuai asumsi ruang lingkup: data hilang saat aplikasi ditutup.
// Semua layar mengakses data lewat object ini, jadi pindah ke Room nanti tidak mengubah UI.
object AnggotaRepository {

    private val daftar = mutableListOf<Anggota>()
    private var idBerikutnya = 1

    init {
        // Data contoh supaya daftar dan pencarian langsung berisi saat demo.
        tambah(Anggota(0, "Andi Pratama", "231001001", "Informatika", "Jl. Merdeka No. 10, Bandung", "081234567890", "andi.pratama@kampus.ac.id", "Mahasiswa", "20 Oktober 2026"))
        tambah(Anggota(0, "Siti Aisyah", "231001002", "Sistem Informasi", "Jl. Diponegoro No. 5, Surabaya", "081298765432", "siti.aisyah@kampus.ac.id", "Mahasiswa", "20 Oktober 2026"))
        tambah(Anggota(0, "Budi Santoso", "198501012010121001", "Fakultas Teknik", "Jl. Sudirman No. 21, Surabaya", "081311112222", "budi.santoso@kampus.ac.id", "Dosen", "20 Oktober 2026"))
        tambah(Anggota(0, "Rina Lestari", "231001003", "Teknik Elektro", "Jl. Ahmad Yani No. 7, Sidoarjo", "085733334444", "rina.lestari@kampus.ac.id", "Mahasiswa", "20 Oktober 2026"))
    }

    fun semua(): List<Anggota> = daftar.toList()

    fun cari(kataKunci: String): List<Anggota> {
        val kunci = kataKunci.trim()
        if (kunci.isEmpty()) return semua()
        return daftar.filter {
            it.nama.contains(kunci, ignoreCase = true) || it.nomorInduk.contains(kunci)
        }
    }

    fun ambil(id: Int): Anggota? = daftar.find { it.id == id }

    // kecualiId dipakai saat edit, supaya nomor induk milik anggota itu sendiri tidak dianggap duplikat.
    fun nomorIndukTerpakai(nomorInduk: String, kecualiId: Int? = null): Boolean =
        daftar.any { it.nomorInduk == nomorInduk && it.id != kecualiId }

    fun tambah(data: Anggota): Anggota {
        val baru = data.copy(id = idBerikutnya++)
        daftar.add(baru)
        return baru
    }

    fun ubah(data: Anggota) {
        val index = daftar.indexOfFirst { it.id == data.id }
        if (index >= 0) daftar[index] = data
    }

    fun hapus(id: Int) {
        daftar.removeAll { it.id == id }
    }
}
