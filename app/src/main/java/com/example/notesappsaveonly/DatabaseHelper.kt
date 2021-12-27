package com.example.notesappsaveonly

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "notesDatabase.db", null, 2) {

    private val sqliteDatabase: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table Notes(pk INTEGER PRIMARY KEY AUTOINCREMENT, Content text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS Notes")
        onCreate(db)
    }

    fun saveData(content: String) {
        val contentValues = ContentValues()
        contentValues.put("Content", content)
        sqliteDatabase.insert("Notes", null, contentValues)
    }

    fun readData(): ArrayList<NoteBook> {
        val noteBook = arrayListOf<NoteBook>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM Notes", null)

        if (cursor.count < 1) {
            println("no data")
        } else {
            while (cursor.moveToNext()) {
                val pk = cursor.getInt(0)
                val note = cursor.getString(1)
                val note1 = NoteBook(pk, note)
                noteBook.add(note1)
            }
        }
        return noteBook
    }

}