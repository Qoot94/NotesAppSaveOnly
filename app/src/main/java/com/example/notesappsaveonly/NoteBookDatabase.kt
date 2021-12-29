package com.example.notesappsaveonly

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteBook::class], version = 2, exportSchema = false)
    abstract class NoteBookDatabase: RoomDatabase() {

        abstract fun notesDoa(): NotesDoa

        companion object{
            @Volatile  // writes to this field are immediately visible to other threads
            private var INSTANCE: NoteBookDatabase? = null

            fun getDatabase(context: Context): NoteBookDatabase{
                val tempInstance = INSTANCE
                if(tempInstance != null){
                    return tempInstance
                }
                synchronized(this){  // protection from concurrent execution on multiple threads
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteBookDatabase::class.java,
                        "Notes"
                    ).fallbackToDestructiveMigration()  // Destroys old database on version change
                        .build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
