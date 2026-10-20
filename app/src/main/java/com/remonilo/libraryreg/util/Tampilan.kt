package com.remonilo.libraryreg.util

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remonilo.libraryreg.R

object Tampilan {

    fun inisial(nama: String): String =
        nama.trim()
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }

    // Badge selalu berisi teks jenis keanggotaan, jadi warna bukan satu-satunya penanda.
    fun terapkanBadge(badge: TextView, jenisKeanggotaan: String) {
        val (latar, teks) = when (jenisKeanggotaan) {
            "Dosen" -> R.color.badge_dosen_bg to R.color.badge_dosen_text
            "Tenaga Kependidikan" -> R.color.badge_tendik_bg to R.color.badge_tendik_text
            else -> R.color.badge_mahasiswa_bg to R.color.badge_mahasiswa_text
        }
        val context = badge.context
        badge.text = jenisKeanggotaan
        badge.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, latar))
        badge.setTextColor(ContextCompat.getColor(context, teks))
    }
}
