package com.example.notesappsaveonly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

open class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val  view=inflater.inflate(R.layout.fragment_main, container, false)
        view.findViewById<Button>(R.id.btStart).setOnClickListener {
            view.visibility = View.GONE
        }
        return view
    }

}