package com.example.notesappsaveonly

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var noteBook: MutableLiveData<List<NoteBook>> = MutableLiveData()
    private val notebookDao by lazy { NoteBookDatabase.getDatabase(application).notesDoa() }

    init {
        noteBook.postValue(listOf(NoteBook(0, "")))
    }

    fun getData(): LiveData<List<NoteBook>> {
        CoroutineScope(IO).launch {
            val data = notebookDao.getNote()
            if (data != null) {
                noteBook.postValue(data)
            }
            Log.e("view-success", "${data}")

        }
        return noteBook
    }

    fun addData(note: NoteBook) {
        viewModelScope.launch(IO) {
            notebookDao.addNote(note)
            noteBook.postValue(notebookDao.getNote())
            Log.e("view-success", "${noteBook}")

        }
    }

    fun updateData(note: NoteBook) {
        CoroutineScope(IO).launch {
            notebookDao.updateNote(note)
            noteBook.postValue(notebookDao.getNote())
            Log.e("update-success", "${noteBook}")
        }
    }

    fun deleteData(note: NoteBook) {
        CoroutineScope(IO).launch {
            notebookDao.deleteNote(note)
        }
    }
}