package com.example.paperscissorsstone

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.paperscissorsstone.databinding.ItemHomefragmentBinding
import com.example.paperscissorsstone.model.PlayRoom

class HomeFragmentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    private val binding = ItemHomefragmentBinding.bind(itemView)

    fun bindTo(playRoom: PlayRoom){
        binding.apply {
            itemRoomname.text = playRoom.name
        }
    }

}