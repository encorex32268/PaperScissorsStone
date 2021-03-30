package com.example.paperscissorsstone.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.model.PlayRoom

class HomeFragmentAdapter(var playRooms : List<PlayRoom>)  : RecyclerView.Adapter<HomeFragmentViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        return HomeFragmentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_homefragment,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = playRooms.size

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        val playRoom = playRooms[position]
        holder.itemView.tag = position
        holder.bindTo(playRoom)
    }

}