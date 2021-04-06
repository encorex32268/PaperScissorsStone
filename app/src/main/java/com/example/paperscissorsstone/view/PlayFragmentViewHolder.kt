package com.example.paperscissorsstone.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.paperscissorsstone.CardTypes
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.ItemPlayfragmentBinding

class PlayFragmentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    private val binding = ItemPlayfragmentBinding.bind(itemView)

    fun bindTo(cardType : CardTypes){
        binding.apply {
            when(cardType){
                CardTypes.PAPER ->itemPlaycard.setImageResource(R.drawable.icon_play_papper)
                CardTypes.SCISSORS->itemPlaycard.setImageResource(R.drawable.icon_play_scissors)
                CardTypes.STONE->itemPlaycard.setImageResource(R.drawable.icon_play_stone)
                CardTypes.UNKOWN -> itemPlaycard.setImageResource(R.drawable.ic_play_unkown)
            }
        }
    }
}