package com.example.notesappsaveonly

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="Notes")
data class NoteBook(@PrimaryKey(autoGenerate = true) val pk: Int, val Note:String)