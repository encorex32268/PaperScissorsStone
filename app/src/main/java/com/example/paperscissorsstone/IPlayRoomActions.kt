package com.example.paperscissorsstone

import com.example.paperscissorsstone.model.PlayRoom

interface IPlayRoomActions {

    fun addPoint(playRoom: PlayRoom, isCreator : Boolean) {}
    fun resetCard(playRoom: PlayRoom){}
    fun changeCard(playRoom: PlayRoom, nowCard : Int, isCreator : Boolean) {}
    fun startGame(playRoom: PlayRoom){}
    fun removeGame(roomIDString: String){}
    fun joinerLeaveRoom(playRoom: PlayRoom) {}
}