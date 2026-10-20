package com.remonilo.libraryreg.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.remonilo.libraryreg.R
import com.remonilo.libraryreg.data.AkunPetugas
import com.remonilo.libraryreg.data.AnggotaRepository
import com.remonilo.libraryreg.databinding.ActivityMemberListBinding

class MemberListActivity : AppCompatActivity() {
    private enum class Kondisi { MEMUAT, ISI, KOSONG, GAGAL }

    private lateinit var binding: ActivityMemberListBinding
    private val adapter =
        MemberAdapter { anggota ->
            startActivity(
                Intent(this, MemberDetailActivity::class.java)
                    .putExtra(MemberDetailActivity.EXTRA_ID, anggota.id),
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layar petugas tidak boleh dibuka tanpa login.
        if (!AkunPetugas.sedangMasuk) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityMemberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.inflateMenu(R.menu.menu_member_list)
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_keluar) {
                keluar()
                true
            } else {
                false
            }
        }

        binding.rvAnggota.layoutManager = LinearLayoutManager(this)
        binding.rvAnggota.adapter = adapter

        binding.etCari.doAfterTextChanged { tampilkan(it?.toString().orEmpty()) }
        binding.fabTambah.setOnClickListener { bukaFormulir() }
        binding.btnTambahKosong.setOnClickListener { bukaFormulir() }
        binding.btnCobaLagi.setOnClickListener { muat() }
    }

    override fun onResume() {
        super.onResume()
        // Dimuat ulang setiap kembali ke layar ini, supaya hasil tambah/edit/hapus langsung terlihat.
        if (::binding.isInitialized) muat()
    }

    private fun muat() {
        aturKondisi(Kondisi.MEMUAT)
        binding.root.post {
            try {
                tampilkan(
                    binding.etCari.text
                        ?.toString()
                        .orEmpty(),
                )
            } catch (e: Exception) {
                aturKondisi(Kondisi.GAGAL)
            }
        }
    }

    private fun tampilkan(kataKunci: String) {
        val semua = AnggotaRepository.semua()
        if (semua.isEmpty()) {
            aturKondisi(Kondisi.KOSONG)
            return
        }
        aturKondisi(Kondisi.ISI)

        val hasil = AnggotaRepository.cari(kataKunci)
        adapter.submitList(hasil)
        binding.tvJumlah.text =
            when {
                kataKunci.isBlank() -> "${semua.size} anggota terdaftar"
                hasil.isEmpty() -> "Anggota dengan kata kunci \"${kataKunci.trim()}\" tidak ditemukan."
                else -> "${hasil.size} anggota ditemukan"
            }
    }

    private fun aturKondisi(kondisi: Kondisi) {
        binding.stateContent.visibility = if (kondisi == Kondisi.ISI) View.VISIBLE else View.GONE
        binding.stateEmpty.visibility = if (kondisi == Kondisi.KOSONG) View.VISIBLE else View.GONE
        binding.stateLoading.visibility = if (kondisi == Kondisi.MEMUAT) View.VISIBLE else View.GONE
        binding.stateError.visibility = if (kondisi == Kondisi.GAGAL) View.VISIBLE else View.GONE
        binding.fabTambah.visibility = if (kondisi == Kondisi.ISI) View.VISIBLE else View.GONE
    }

    private fun bukaFormulir() {
        startActivity(Intent(this, FormActivity::class.java))
    }

    private fun keluar() {
        AkunPetugas.keluar()
        Toast.makeText(this, "Anda telah keluar", Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
        )
        finish()
    }
}
