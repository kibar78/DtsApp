package com.example.kalkulator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kalkulator.database.DatabaseHelper
import com.example.kalkulator.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val checkEmail = db.checkEmail(email)
            if (!checkEmail) {
                val insert = db.insertUser(email, password)
                if (insert) {
                    Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Email Already Exists", Toast.LENGTH_SHORT).show()
            }
        }

    }
}