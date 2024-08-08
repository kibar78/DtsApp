package com.example.kalkulator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "app_database"

        // User Table
        private const val USER_TABLE_NAME = "user_table"
        private const val USER_COL_1 = "ID"
        private const val USER_COL_2 = "EMAIL"
        private const val USER_COL_3 = "PASSWORD"

        // Notes Table
        private const val NOTES_TABLE_NAME = "notes"
        private const val NOTES_COLUMN_ID = "id"
        private const val NOTES_COLUMN_TITLE = "title"
        private const val NOTES_COLUMN_CONTENT = "content"
        private const val NOTES_COLUMN_DATE = "date"

    }


    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(
            "CREATE TABLE $USER_TABLE_NAME (" +
                    "$USER_COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$USER_COL_2 TEXT, " +
                    "$USER_COL_3 TEXT)"
        )

        p0?.execSQL(
            "CREATE TABLE $NOTES_TABLE_NAME (" +
                    "$NOTES_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NOTES_COLUMN_TITLE TEXT, " +
                    "$NOTES_COLUMN_CONTENT TEXT, " +
                    "$NOTES_COLUMN_DATE TEXT)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        p0?.execSQL("DROP TABLE IF EXISTS $NOTES_TABLE_NAME")
        onCreate(p0)
    }

    fun insertUser(email: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(USER_COL_2, email)
            put(USER_COL_3, password)
        }
        val result = db.insert(USER_TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun checkEmail(email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $USER_TABLE_NAME WHERE EMAIL = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkEmailPassword(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $USER_TABLE_NAME WHERE EMAIL = ? AND PASSWORD = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Notes Methods
    fun addNote(note: Note): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NOTES_COLUMN_TITLE, note.title)
            put(NOTES_COLUMN_CONTENT, note.content)
            put(NOTES_COLUMN_DATE, note.date)
        }
        val id = db.insert(NOTES_TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getNote(id: Int): Note {
        val db = this.readableDatabase
        val cursor = db.query(
            NOTES_TABLE_NAME, arrayOf(NOTES_COLUMN_ID, NOTES_COLUMN_TITLE, NOTES_COLUMN_CONTENT, NOTES_COLUMN_DATE),
            "$NOTES_COLUMN_ID=?", arrayOf(id.toString()), null, null, null, null
        )
        cursor?.moveToFirst()
        val note = Note(
            cursor.getInt(cursor.getColumnIndexOrThrow(NOTES_COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_TITLE)),
            cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_CONTENT)),
            cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_DATE))
        )
        cursor.close()
        return note
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val selectQuery = "SELECT * FROM $NOTES_TABLE_NAME ORDER BY $NOTES_COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(NOTES_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_DATE))
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
            put(NOTES_COLUMN_TITLE, note.title)
            put(NOTES_COLUMN_CONTENT, note.content)
            put(NOTES_COLUMN_DATE, note.date)
        }
        return db.update(NOTES_TABLE_NAME, values, "$NOTES_COLUMN_ID = ?", arrayOf(note.id.toString()))
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        db.delete(NOTES_TABLE_NAME, "$NOTES_COLUMN_ID = ?", arrayOf(note.id.toString()))
        db.close()
    }

    fun searchNotes(keyword: String): List<Note> {
        val notes = mutableListOf<Note>()
        val searchQuery =
            "SELECT * FROM $NOTES_TABLE_NAME WHERE $NOTES_COLUMN_TITLE LIKE ? OR $NOTES_COLUMN_CONTENT LIKE ? ORDER BY $NOTES_COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(searchQuery, arrayOf("%$keyword%", "%$keyword%"))
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val note = Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(NOTES_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTES_COLUMN_DATE))
                    )
                    notes.add(note)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return notes
        }
    }

}