package com.remonilo.libraryreg.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.remonilo.libraryreg.R
import com.remonilo.libraryreg.data.Anggota
import com.remonilo.libraryreg.data.AnggotaRepository
import com.remonilo.libraryreg.databinding.ActivityFormBinding
import com.remonilo.libraryreg.util.Validator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Satu layar dipakai untuk pendaftaran baru dan edit; mode edit aktif jika EXTRA_ID dikirim.
class FormActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"

        val PROGRAM_STUDI = listOf(
            "Informatika",
            "Sistem Informasi",
            "Rekayasa Perangkat Lunak",
            "Teknik Elektro",
            "Teknik Komputer",
            "Fakultas Teknik",
        )
        val JENIS_KEANGGOTAAN = listOf("Mahasiswa", "Dosen", "Tenaga Kependidikan")
    }

    private lateinit var binding: ActivityFormBinding
    private var anggotaLama: Anggota? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnBatal.setOnClickListener { finish() }
        binding.btnSimpan.setOnClickListener { simpan() }

        siapkanSpinner(binding.spJenis, binding.frameJenis, "Pilih jenis keanggotaan", JENIS_KEANGGOTAAN, binding.tvJenisError)
        siapkanSpinner(binding.spProdi, binding.frameProdi, "Pilih program studi / unit", PROGRAM_STUDI, binding.tvProdiError)

        hapusErrorSaatDiketik(binding.etNama, binding.tilNama)
        hapusErrorSaatDiketik(binding.etNomorInduk, binding.tilNomorInduk)
        hapusErrorSaatDiketik(binding.etAlamat, binding.tilAlamat)
        hapusErrorSaatDiketik(binding.etTelepon, binding.tilTelepon)
        hapusErrorSaatDiketik(binding.etEmail, binding.tilEmail)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        anggotaLama = if (id != -1) AnggotaRepository.ambil(id) else null
        anggotaLama?.let { isiUntukEdit(it) }
    }

    private fun isiUntukEdit(anggota: Anggota) {
        binding.toolbar.title = "Edit Anggota"
        binding.tvIntro.text = "Ubah data yang perlu diperbarui, lalu simpan."
        binding.btnSimpan.text = "Simpan perubahan"
        binding.btnBatal.visibility = View.VISIBLE

        binding.etNama.setText(anggota.nama)
        binding.etNomorInduk.setText(anggota.nomorInduk)
        binding.etAlamat.setText(anggota.alamat)
        binding.etTelepon.setText(anggota.telepon)
        binding.etEmail.setText(anggota.email)
        // Posisi 0 adalah teks petunjuk, jadi indeks data digeser satu.
        binding.spJenis.setSelection(JENIS_KEANGGOTAAN.indexOf(anggota.jenisKeanggotaan) + 1)
        binding.spProdi.setSelection(PROGRAM_STUDI.indexOf(anggota.programStudi) + 1)
    }

    private fun simpan() {
        val nama = binding.etNama.text.toString().trim()
        val nomorInduk = binding.etNomorInduk.text.toString().trim()
        val alamat = binding.etAlamat.text.toString().trim()
        val telepon = binding.etTelepon.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val jenis = nilaiTerpilih(binding.spJenis, JENIS_KEANGGOTAAN)
        val prodi = nilaiTerpilih(binding.spProdi, PROGRAM_STUDI)

        val errorKolom = listOf(
            binding.tilNama to Validator.wajib(nama, "Nama lengkap"),
            binding.tilNomorInduk to Validator.nomorInduk(nomorInduk, jenis, anggotaLama?.id),
            binding.tilAlamat to Validator.wajib(alamat, "Alamat"),
            binding.tilTelepon to Validator.telepon(telepon),
            binding.tilEmail to Validator.email(email),
        )
        errorKolom.forEach { (til, pesan) -> til.error = pesan }

        val errorJenis = Validator.pilihan(jenis, "Jenis keanggotaan")
        val errorProdi = Validator.pilihan(prodi, "Program studi / unit")
        tampilkanErrorSpinner(binding.frameJenis, binding.tvJenisError, errorJenis)
        tampilkanErrorSpinner(binding.frameProdi, binding.tvProdiError, errorProdi)

        val jumlahError = errorKolom.count { it.second != null } + listOfNotNull(errorJenis, errorProdi).size
        if (jumlahError > 0) {
            binding.tvBannerError.text = "$jumlahError kolom perlu diperbaiki sebelum data bisa disimpan."
            binding.bannerError.visibility = View.VISIBLE
            binding.scroll.smoothScrollTo(0, 0)
            return
        }
        binding.bannerError.visibility = View.GONE

        val lama = anggotaLama
        if (lama == null) {
            AnggotaRepository.tambah(
                Anggota(0, nama, nomorInduk, prodi, alamat, telepon, email, jenis, tanggalHariIni())
            )
            Toast.makeText(this, "Pendaftaran berhasil. Selamat bergabung, $nama!", Toast.LENGTH_LONG).show()
        } else {
            AnggotaRepository.ubah(
                lama.copy(
                    nama = nama,
                    nomorInduk = nomorInduk,
                    programStudi = prodi,
                    alamat = alamat,
                    telepon = telepon,
                    email = email,
                    jenisKeanggotaan = jenis,
                )
            )
            Toast.makeText(this, "Perubahan data berhasil disimpan", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun siapkanSpinner(spinner: Spinner, bingkai: View, petunjuk: String, pilihan: List<String>, tvError: TextView) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf(petunjuk) + pilihan)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) tampilkanErrorSpinner(bingkai, tvError, null)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun nilaiTerpilih(spinner: Spinner, pilihan: List<String>): String {
        val posisi = spinner.selectedItemPosition
        return if (posisi > 0) pilihan[posisi - 1] else ""
    }

    private fun tampilkanErrorSpinner(bingkai: View, tvError: TextView, pesan: String?) {
        tvError.text = pesan
        tvError.visibility = if (pesan == null) View.GONE else View.VISIBLE
        bingkai.setBackgroundResource(if (pesan == null) R.drawable.bg_spinner else R.drawable.bg_spinner_error)
    }

    private fun hapusErrorSaatDiketik(editText: TextInputEditText, til: TextInputLayout) {
        editText.doAfterTextChanged { til.error = null }
    }

    private fun tanggalHariIni(): String =
        SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(Date())
}
