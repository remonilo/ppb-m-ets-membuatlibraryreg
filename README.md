# LibraryReg

Aplikasi Android pendaftaran anggota perpustakaan untuk ETS Pemrograman Perangkat Bergerak (M).
Dibuat dengan Kotlin dan Layout XML (View system), minimal Android 7.0 (API 24).

## Cara menjalankan

1. Ekstrak folder ini, lalu buka di Android Studio lewat **File → Open** dan pilih folder `LibraryReg`.
2. Tunggu proses Gradle sync selesai. Jika diminta, izinkan Android Studio mengunduh SDK Platform 34.
3. Jalankan di emulator atau HP Android dengan tombol **Run**.

## Akun petugas (ditulis di kode)

| Username | Password |
|---|---|
| petugas1 | perpus123 |
| petugas2 | perpus456 |

## Struktur project

```
app/src/main/java/com/remonilo/libraryreg/
├── data/
│   ├── Anggota.kt              data class anggota
│   ├── AnggotaRepository.kt    penyimpanan in-memory: tambah, tampil, cari, ubah, hapus
│   └── AkunPetugas.kt          akun petugas dan status login
├── util/
│   ├── Validator.kt            aturan validasi formulir
│   └── Tampilan.kt             inisial nama dan warna badge
└── ui/
    ├── MainActivity.kt         Beranda
    ├── LoginActivity.kt        Login Petugas
    ├── FormActivity.kt         Formulir Pendaftaran dan Edit Anggota
    ├── MemberListActivity.kt   Daftar Anggota, pencarian, kondisi kosong/memuat/gagal
    ├── MemberAdapter.kt        Adapter RecyclerView
    └── MemberDetailActivity.kt Detail Anggota dan dialog hapus

app/src/main/res/layout/        tampilan XML setiap layar
```

## Aturan validasi

- Semua kolom wajib diisi.
- NIM/NIP hanya angka: 9 digit untuk Mahasiswa, 18 digit untuk Dosen dan Tenaga Kependidikan.
- NIM/NIP harus unik.
- Nomor telepon diawali 08, panjang 10 sampai 13 digit.
- Email harus berformat valid, contoh `nama@kampus.ac.id`.

## Catatan

- Data disimpan di memori, jadi kembali ke 4 data contoh setiap aplikasi dibuka ulang.
- Menekan tombol kembali dari Daftar Anggota tidak mengeluarkan petugas. Gunakan tombol **Keluar** untuk logout.
