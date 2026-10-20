package com.remonilo.libraryreg.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.remonilo.libraryreg.data.Anggota
import com.remonilo.libraryreg.data.AnggotaRepository
import com.remonilo.libraryreg.databinding.ActivityMemberDetailBinding
import com.remonilo.libraryreg.databinding.ItemDetailRowBinding
import com.remonilo.libraryreg.util.Tampilan

class MemberDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityMemberDetailBinding
    private var idAnggota = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idAnggota = intent.getIntExtra(EXTRA_ID, -1)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnEdit.setOnClickListener {
            startActivity(
                Intent(this, FormActivity::class.java).putExtra(FormActivity.EXTRA_ID, idAnggota)
            )
        }
        binding.btnHapus.setOnClickListener {
            AnggotaRepository.ambil(idAnggota)?.let { konfirmasiHapus(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        // Data dibaca ulang supaya perubahan dari layar edit langsung tampil.
        val anggota = AnggotaRepository.ambil(idAnggota)
        if (anggota == null) {
            finish()
            return
        }
        tampilkan(anggota)
    }

    private fun tampilkan(anggota: Anggota) {
        binding.tvAvatar.text = Tampilan.inisial(anggota.nama)
        binding.tvNama.text = anggota.nama
        Tampilan.terapkanBadge(binding.tvBadge, anggota.jenisKeanggotaan)

        val baris = listOf(
            "NIM/NIP" to anggota.nomorInduk,
            "Program studi / unit" to anggota.programStudi,
            "Alamat" to anggota.alamat,
            "Nomor telepon" to anggota.telepon,
            "Email" to anggota.email,
            "Tanggal daftar" to anggota.tanggalDaftar,
        )
        binding.containerDetail.removeAllViews()
        baris.forEach { (label, nilai) ->
            val row = ItemDetailRowBinding.inflate(layoutInflater, binding.containerDetail, false)
            row.tvLabel.text = label
            row.tvValue.text = nilai
            binding.containerDetail.addView(row.root)
        }
    }

    private fun konfirmasiHapus(anggota: Anggota) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus anggota?")
            .setMessage("Data ${anggota.nama} (${anggota.nomorInduk}) akan dihapus permanen dan tidak bisa dikembalikan.")
            .setNegativeButton("Batal", null)
            .setPositiveButton("Hapus") { _, _ ->
                AnggotaRepository.hapus(anggota.id)
                Toast.makeText(this, "Data ${anggota.nama} telah dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
            .show()
    }
}
