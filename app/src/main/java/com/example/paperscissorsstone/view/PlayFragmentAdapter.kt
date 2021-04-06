package com.example.paperscissorsstone.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.paperscissorsstone.CardTypes
import com.example.paperscissorsstone.IPlayItemListener
import com.example.paperscissorsstone.R

class PlayFragmentAdapter(var cards : ArrayList<CardTypes>) : RecyclerView.Adapter<PlayFragmentViewHolder>(){

    var iPlayItemListener : IPlayItemListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayFragmentViewHolder {
        return PlayFragmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_playfragment,parent,false))
    }

    override fun getItemCount()  = cards.size

    override fun onBindViewHolder(holder: PlayFragmentViewHolder, position: Int) {
        val cardTypes = cards[position]
        holder.bindTo(cardTypes)
        holder.itemView.setOnClickListener {
            Log.d("PlayFragmentAdapter", "onBindViewHolder: ${cardTypes.name}")
            iPlayItemListener?.onItemClick(cardTypes)
        }
    }

}