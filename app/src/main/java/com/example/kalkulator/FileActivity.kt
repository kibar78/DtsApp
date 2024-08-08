package com.example.kalkulator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kalkulator.databinding.ActivityFileBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException

class FileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileBinding

    val FILE_NAME = "dts.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBuatFile.setOnClickListener {
            buatFile()
        }
        binding.btnBacaFile.setOnClickListener {
            bacaFile()
        }
        binding.btnUbahFile.setOnClickListener {
            ubahFile()
        }
        binding.btnHapusFile.setOnClickListener {
            hapusFile()
        }
    }
    fun bacaFile() {
        val sdcard: File = filesDir
        val file = File(sdcard, FILE_NAME)
        if (file.exists()) {
            val text = StringBuilder()
            try {
                val br = BufferedReader(FileReader(file))
                var line: String? = br.readLine()
                while (line != null) {
                    text.append(line)
                    line = br.readLine()
                }
                br.close()
            } catch (e: IOException) {
                println("Error " + e.message)
            }
            // Assuming tvBaca is a TextView
            binding.tvBaca.text
        }
    }

    fun buatFile() {
        val isiFile = binding.tvBaca.text.toString()
        val file = File(filesDir, FILE_NAME)
        try {
            file.createNewFile()
            FileOutputStream(file, true).use { outputStream ->
                outputStream.write(isiFile.toByteArray())
                outputStream.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun ubahFile() {
        val ubah = "ubah data text"
        val file = File(filesDir, FILE_NAME)
        try {
            file.createNewFile()
            FileOutputStream(file, false).use { outputStream ->
                outputStream.write(ubah.toByteArray())
                outputStream.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hapusFile() {
        val file = File(filesDir, FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }
}