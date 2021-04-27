package com.example.paperscissorsstone

import com.example.paperscissorsstone.model.PlayRoom

interface IPlayRoomActions {

    fun addPointCreator(playRoom: PlayRoom)
    fun addPointJoiner(playRoom: PlayRoom)
    fun resetCard(playRoom: PlayRoom)
    fun changeCard(playRoom: PlayRoom, nowCard : Int, isCreator : Boolean)
    fun startGame(playRoom: PlayRoom)
    fun removeGame(roomIDString: String)
    fun joinerLeaveRoom(playRoom: PlayRoom)
    fun updateStatus(playRoom: PlayRoom)
}