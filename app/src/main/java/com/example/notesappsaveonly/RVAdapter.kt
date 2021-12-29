package com.example.notesappsaveonly


import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappsaveonly.databinding.ItemRowBinding
import android.widget.EditText

import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RVAdapter() :
    RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    private var noteBook = emptyList<NoteBook>()
    lateinit var viewContext: Context

    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        viewContext = parent.context
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val notebookDao by lazy { NoteBookDatabase.getDatabase(viewContext).notesDoa() }
        val cards = noteBook[position]
        holder.binding.apply {
            tvContent.text = cards.Note
            tvPk.text = cards.pk.toString()

            //cont. db CRUD: create button interactions
            //update
            ibUpdate.setOnClickListener {
                var updatedNoteBook: NoteBook? = null
                // first we create a variable to hold an AlertDialog builder
                val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(viewContext)
                // then we set up the input
                val input = EditText(this@RVAdapter.viewContext)
                // here we set the message of our alert dialog
                dialogBuilder.setMessage("Enter your updated note:")
                    // positive button text and action
                    .setPositiveButton("save", DialogInterface.OnClickListener { _, id ->
                        if ((input.text.isNotEmpty()) || (input.equals(""))) {
                            updatedNoteBook =
                                NoteBook(pk = cards.pk, input.text.toString())
                            var noteList = listOf<NoteBook>()
                            val launch = CoroutineScope(IO).launch {
                                launch {
                                    notebookDao.updateNote(updatedNoteBook!!)
                                    noteList = notebookDao.getNote()
                                }
                                withContext(Main) {
                                    update(noteList)
                                }
                            }

                            Toast.makeText(viewContext, "Note updated", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(viewContext, "Please enter a value", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Update")
                // add the Edit Text
                alert.setView(input)
                // show alert dialog
                alert.show()
            }
            //delete
            ibDelete.setOnClickListener {
                //delete confirmation dialog
                val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(viewContext)
                dialogBuilder.setMessage("Are you sure you want to delete note no. ${cards.pk} ?")
                    // if yes button action is clicked
                    .setPositiveButton("yes", DialogInterface.OnClickListener { _, id ->
                        var noteList = listOf<NoteBook>()
                        CoroutineScope(IO).launch {
                            launch {
                                notebookDao.deleteNote(noteBook[position])
                                noteList = notebookDao.getNote()
                            }
                            withContext(Main) {
                                update(noteList)
                            }
                        }
                        Toast.makeText(
                            viewContext,
                            "note successfully deleted from database",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                    //if no
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Delete")
                // show alert dialog
                alert.show()
            }
        }
    }

    override fun getItemCount(): Int = noteBook.size

    fun update(notes: List<NoteBook>) {
        this.noteBook = notes
        this.notifyDataSetChanged()
    }
}
