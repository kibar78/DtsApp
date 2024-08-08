package com.example.kalkulator.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseNote(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "notes_db"
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_TITLE TEXT, "
                + "$COLUMN_CONTENT TEXT, "
                + "$COLUMN_DATE TEXT)")
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
    fun addNote(note: Note): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.date)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getNote(id: Int): Note {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_DATE),
            "$COLUMN_ID=?", arrayOf(id.toString()), null, null, null, null
        )
        cursor?.moveToFirst()
        val note = Note(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        )
        cursor.close()
        return note
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                )
                notes.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return notes
    }

    fun updateNote(note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.date)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
        db.close()
    }

    fun searchNotes(keyword: String): List<Note> {
        val notes = mutableListOf<Note>()
        val searchQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE LIKE ? OR $COLUMN_CONTENT LIKE ? ORDER BY $COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(searchQuery, arrayOf("%$keyword%", "%$keyword%"))
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val note = Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                    )
                    notes.add(note)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return notes
        }
    }
}