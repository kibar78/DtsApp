package com.example.kalkulator

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.kalkulator.database.DatabaseNote
import com.example.kalkulator.database.Note
import com.example.kalkulator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private lateinit var db: DatabaseNote
    private lateinit var notesList: List<Note>
    private lateinit var adapter: ArrayAdapter<String>
    private var titlesList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setOnMenuItemClickListener {menuItem->
            when(menuItem.itemId){
                R.id.kalkulator->{
                    val goMessage = Intent(this,KalkulatorActivity::class.java)
                    startActivity(goMessage)
                    true
                }
                R.id.menyimpan_file->{
                    val goSaveFile = Intent(this, FileActivity::class.java)
                    startActivity(goSaveFile)
                    true
                }
                else -> false
            }
        }

        db = DatabaseNote(this)

        binding.fab.setOnClickListener {
            val goEdit = Intent(this, AddEditActivity::class.java)
            startActivity(goEdit)
        }

        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("note_id", notesList[i].id)
            startActivity(intent)
        }

        binding.listView.setOnItemLongClickListener { _, _, position, _ ->
            db.deleteNote(notesList[position])
            loadNotes()
            true
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchNotes(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        }


    private fun loadNotes() {
        notesList = db.getAllNotes()
        titlesList.clear()
        for (note in notesList) {
            titlesList.add(note.title + "\n" + note.date)
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titlesList)
        binding.listView.adapter = adapter
    }

    private fun searchNotes(keyword: String) {
        notesList = db.searchNotes(keyword).toMutableList()
        titlesList.clear()
        for (note: Note in notesList) {
            titlesList.add(note.title)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

}