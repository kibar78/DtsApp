package com.example.kalkulator.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "User.db"
        const val TABLE_NAME = "user_table"
        const val COL_1 = "ID"
        const val COL_2 = "EMAIL"
        const val COL_3 = "PASSWORD"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PASSWORD TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    fun insert(email: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_2, email)
            put(COL_3, password)
        }
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun checkEmail(email: String): Boolean {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE EMAIL = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkEmailPassword(email: String, password: String): Boolean {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE EMAIL = ? AND PASSWORD = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

}