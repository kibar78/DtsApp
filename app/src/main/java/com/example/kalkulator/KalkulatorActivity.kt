package com.example.kalkulator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kalkulator.databinding.ActivityKalkulatorBinding

class KalkulatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKalkulatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKalkulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTambah.setOnClickListener {
            val angkaPertama = binding.edtAngkaPertama.text.toString().toDoubleOrNull()
            val angkaKedua = binding.edtAngkaKedua.text.toString().toDoubleOrNull()
            val result = angkaPertama?.plus(angkaKedua ?: 0.0)
            binding.tvResult.text = result.toString()
        }

        binding.btnKurang.setOnClickListener {
            val angkaPertama = binding.edtAngkaPertama.text.toString().toDoubleOrNull()
            val angkaKedua = binding.edtAngkaKedua.text.toString().toDoubleOrNull()
            val result = angkaPertama?.minus(angkaKedua ?: 0.0)
            binding.tvResult.text = result.toString()
        }

        binding.btnKali.setOnClickListener {
            val angkaPertama = binding.edtAngkaPertama.text.toString().toDoubleOrNull()
            val angkaKedua = binding.edtAngkaKedua.text.toString().toDoubleOrNull()
            val result = angkaPertama?.times(angkaKedua ?: 0.0)
            binding.tvResult.text = result.toString()
        }

        binding.btnBagi.setOnClickListener {
            val angkaPertama = binding.edtAngkaPertama.text.toString().toDoubleOrNull()
            val angkaKedua = binding.edtAngkaKedua.text.toString().toDoubleOrNull()
            val result = angkaPertama?.div(angkaKedua ?: 0.0)
            binding.tvResult.text = result.toString()
        }

        binding.btnBersihkan.setOnClickListener {
            binding.edtAngkaPertama.text.clear()
            binding.edtAngkaKedua.text.clear()
            binding.tvResult.text = "0"
        }

    }
}