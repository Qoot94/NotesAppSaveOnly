package com.example.notesappsaveonly

import androidx.room.*

@Dao
interface NotesDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: NoteBook)

    @Query("SELECT * FROM Notes ORDER BY pk ASC")
    fun getNote(): List<NoteBook>

    @Update
    suspend fun updateNote(note: NoteBook)

    @Delete
    suspend fun deleteNote(note: NoteBook)
}