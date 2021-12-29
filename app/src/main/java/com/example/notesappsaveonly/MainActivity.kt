package com.example.notesappsaveonly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var myRV: RecyclerView
    lateinit var rvAdapter: RVAdapter
    private lateinit var etNote: EditText
    private lateinit var btSave: Button
    private lateinit var noteBook: ArrayList<NoteBook>

    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        noteBook = arrayListOf<NoteBook>()
        //set up UI
        myRV = findViewById(R.id.rvMain)
        etNote = findViewById(R.id.etNote)
        btSave = findViewById(R.id.btSave)

        //db CRUD: create button interactions
        //create
        btSave.setOnClickListener {
            val note: String = etNote.text.toString()
            databaseHelper.saveData(note)
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
        noteBook = databaseHelper.readData()
        rvAdapter.update(noteBook)
    }
}