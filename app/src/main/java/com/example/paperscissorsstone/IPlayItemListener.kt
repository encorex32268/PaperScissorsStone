package com.example.paperscissorsstone

import com.example.paperscissorsstone.model.PlayRoom

interface IPlayItemListener {
    fun onItemClick(cardTypes: CardTypes)
}