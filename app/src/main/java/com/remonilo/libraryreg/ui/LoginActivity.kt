package com.remonilo.libraryreg.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.remonilo.libraryreg.data.AkunPetugas
import com.remonilo.libraryreg.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnMasuk.setOnClickListener { masuk() }
        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                masuk()
                true
            } else {
                false
            }
        }

        binding.etUsername.doAfterTextChanged { binding.tilUsername.error = null }
        binding.etPassword.doAfterTextChanged { binding.tilPassword.error = null }
    }

    private fun masuk() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.tilUsername.error = if (username.isEmpty()) "Username wajib diisi" else null
        binding.tilPassword.error = if (password.isEmpty()) "Password wajib diisi" else null
        if (username.isEmpty() || password.isEmpty()) return

        if (AkunPetugas.masuk(username, password)) {
            binding.bannerError.visibility = View.GONE
            startActivity(Intent(this, MemberListActivity::class.java))
            finish()
        } else {
            binding.bannerError.visibility = View.VISIBLE
            binding.etPassword.text?.clear()
            binding.tilPassword.error = "Password tidak cocok dengan username"
            binding.etPassword.requestFocus()
        }
    }
}
