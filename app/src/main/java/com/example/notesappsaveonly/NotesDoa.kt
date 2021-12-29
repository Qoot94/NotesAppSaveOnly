package com.example.notesappsaveonly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface NotesDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun addNote(note: NoteBook)

    @Query("SELECT * FROM Notes ORDER BY pk ASC")
    fun getNote(): List<NoteBook>

    @Update
     fun updateNote(note: NoteBook)

    @Delete
     fun deleteNote(note: NoteBook)
}