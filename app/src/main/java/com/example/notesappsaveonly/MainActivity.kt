package com.example.notesappsaveonly

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var myRV: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    private lateinit var etNote: EditText
    private lateinit var btSave: Button
    private lateinit var noteBook: List<NoteBook>

    private val notebookDao by lazy { NoteBookDatabase.getDatabase(this).notesDoa() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        noteBook = arrayListOf()
        //set up UI
        myRV = findViewById(R.id.rvMain)
        etNote = findViewById(R.id.etNote)
        btSave = findViewById(R.id.btSave)

        //db CRUD: create button interactions
        //create
        btSave.setOnClickListener {
            val note: String = etNote.text.toString()
            CoroutineScope(IO).launch {
                notebookDao.addNote(NoteBook(0, note))

            }
            Toast.makeText(this, "Added successfully to database", Toast.LENGTH_SHORT).show()
            refreshTable()
            etNote.text.clear()
        }

        rvAdapter = RVAdapter()
        myRV.adapter = rvAdapter
        myRV.layoutManager = LinearLayoutManager(applicationContext)
        //read
        refreshTable()
    }

    private fun refreshTable() {
        CoroutineScope(IO).launch {
            val data = withContext(Dispatchers.Default) {
                notebookDao.getNote()
            }
            if (data.isNotEmpty()) {
                noteBook = data
                withContext(Main) {
                    rvAdapter.update(noteBook)
                }
            } else {
                Log.e("Main-error", "unable to get data")
            }
        }
        rvAdapter.update(noteBook)
    }
}