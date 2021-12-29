package com.example.notesappsaveonly

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappsaveonly.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var myRV: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    private lateinit var etNote: EditText
    private lateinit var btSave: Button

    //    lateinit var noteBooks: List<NoteBook>
    lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set ui elements
        myRV = binding.rvMain
        etNote = binding.etNote
        btSave = binding.btSave
//        noteBooks = listOf()
        rvAdapter = RVAdapter(this)


        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        //read
        myViewModel.getData().observe(this, { noteBook -> rvAdapter.update(noteBook) })
        setView()
        //db CRUD: create button interactions
        //create
        btSave.setOnClickListener {
            val note: String = etNote.text.toString()
            myViewModel.addData(NoteBook(pk = 0, note))
            etNote.text.clear()
            setView()
            Toast.makeText(this, "Added successfully to database", Toast.LENGTH_SHORT)
                .show()

        }


    }

    private fun setView() {
        myRV.adapter = rvAdapter
        myRV.layoutManager = LinearLayoutManager(applicationContext)
        myViewModel.getData().observe(this, { noteBook -> rvAdapter.update(noteBook) })

    }
}