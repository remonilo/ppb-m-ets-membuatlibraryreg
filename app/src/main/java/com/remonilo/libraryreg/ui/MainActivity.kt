package com.remonilo.libraryreg.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.remonilo.libraryreg.data.AkunPetugas
import com.remonilo.libraryreg.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardDaftar.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }

        binding.cardPetugas.setOnClickListener {
            val tujuan = if (AkunPetugas.sedangMasuk) MemberListActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, tujuan))
        }
    }
}
