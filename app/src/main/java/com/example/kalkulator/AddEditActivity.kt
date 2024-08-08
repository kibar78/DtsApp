package com.example.kalkulator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kalkulator.database.DatabaseNote
import com.example.kalkulator.database.Note
import com.example.kalkulator.databinding.ActivityAddEditBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding

    private lateinit var db: DatabaseNote
    private var note: Note? = null
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseNote(this)

        if (intent.hasExtra("note_id")) {
            val noteId = intent.getIntExtra("note_id", -1)
            note = db.getNote(noteId)
            note.let {
                binding.etTitle.setText(it?.title)
                binding.etContent.setText(it?.content)
                isEdit = true
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            if (isEdit) {
                note.let {
                    it?.title = title
                    it?.content = content
                    it?.date = date
                    if (it != null) {
                        db.updateNote(it)
                    }
                }
            }else{
                note = Note(title = title, content = content, date = date)
                db.addNote(note!!)
            }
            finish()
        }
    }

}

