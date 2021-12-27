package com.example.notesappsaveonly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var etNote: EditText
    private lateinit var btSave: Button
    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //set up UI
        etNote = findViewById(R.id.etNote)
        btSave = findViewById(R.id.btSave)

        //create button interactions
        btSave.setOnClickListener {
            val note = etNote.text.toString()
            databaseHelper.saveData(note)
            Toast.makeText(this, "Added successfully to database", Toast.LENGTH_SHORT).show()
        }
    }
}