package com.example.notesappsaveonly


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.notesappsaveonly.databinding.ItemRowBinding


class RVAdapter() :
    RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    private var noteBook = emptyList<NoteBook>()

    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val cards = noteBook[position]
        holder.binding.apply {
            tvContent.text = cards.Note
        }
    }

    override fun getItemCount(): Int = noteBook.size

    fun update(notes: ArrayList<NoteBook>) {
        this.noteBook = notes
        notifyDataSetChanged()
    }
}
