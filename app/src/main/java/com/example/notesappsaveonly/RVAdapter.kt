package com.example.notesappsaveonly


import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappsaveonly.databinding.ItemRowBinding
import android.widget.EditText

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.ViewModelStoreOwner

import androidx.lifecycle.ViewModelProvider


class RVAdapter(
    val main: MainActivity
) :
    RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    private var noteBooks = emptyList<NoteBook>()
    lateinit var viewContext: Context
    var myViewModel = MyViewModel(application = Application())
//    val myViewModel = ViewModelProvider(viewContext.applicationContext).get(MyViewModel::class.java)

    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        viewContext = parent.context
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (myViewModel == null) {
            myViewModel = ViewModelProvider((recyclerView.context as ViewModelStoreOwner)).get(
                MyViewModel::class.java
            )
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val cards = noteBooks[position]
        holder.binding.apply {
            tvContent.text = cards.Note
            tvPk.text = cards.pk.toString()
            Log.d("rv-Main", "${cards.Note} ")

            //cont. db CRUD: create button interactions
            //update
            ibUpdate.setOnClickListener {
                // first we create a variable to hold an AlertDialog builder
                val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(viewContext)
                val input = EditText(this@RVAdapter.viewContext)
                input.setText(cards.Note)
                // here we set the message of our alert dialog
                dialogBuilder.setMessage("Enter your updated note:")
                    // positive button text and action
                    .setPositiveButton("save", DialogInterface.OnClickListener { _, id ->
                        if ((input.text.isNotEmpty()) || (input.equals(""))) {
                            val updatedNoteBook =
                                NoteBook(pk = cards.pk, input.text.toString())
                            myViewModel.updateData(updatedNoteBook)
                            myViewModel.getData()
                                .observe(main, {noteBook-> this@RVAdapter.update(noteBook)})
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
                        myViewModel.deleteData(cards)

                        myViewModel.getData()
                            .observe(main, {noteBook-> this@RVAdapter.update(noteBook)})

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

    override fun getItemCount(): Int = noteBooks.size

    fun update(notes: List<NoteBook>) {
        this.noteBooks = notes
        this.notifyDataSetChanged()
    }
}
