package com.example.paperscissorsstone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.paperscissorsstone.Constants
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS
import com.example.paperscissorsstone.IPlayRoomActions

import com.example.paperscissorsstone.model.PlayRoom
import com.google.firebase.database.*

class PlayFragmentViewModel(application: Application)  : AndroidViewModel(application),
    IPlayRoomActions {

    var playRoom = MutableLiveData<PlayRoom>()
    private var mRef : DatabaseReference = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)

    fun getPlayRoomInfo(roomIDString : String) : LiveData<PlayRoom>{
        mRef.child(roomIDString)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playroom = snapshot.getValue(PlayRoom::class.java)
                    if (playroom == null){
                        playRoom.value = PlayRoom()
                    }else{
                        playRoom.value = playroom!!
                    }
                }

            })
        return playRoom
    }

    private fun updatePlayRoom(playRoom: PlayRoom){
        mRef.child(playRoom.id.toString()).setValue(playRoom)
    }

    override fun addPoint(playRoom: PlayRoom,isCreator : Boolean) {
        playRoom.apply {
            if(isCreator){ creatorPoint++ }else{ joinerPoint ++ }
            creatorCard = 99
            joinerCard = 99
        }
        updatePlayRoom(playRoom)
    }
    override fun resetCard(playRoom: PlayRoom){
        playRoom.apply {
            creatorCard = 99
            joinerCard = 99
        }
        updatePlayRoom(playRoom)
    }
    override fun changeCard(playRoom: PlayRoom,nowCard : Int,isCreator : Boolean) {
        playRoom.apply {
            if(isCreator){
                creatorCard = nowCard
                status = Constants.PLAYROOM_STATUS_CREATOR_OK
            }else{
                joinerCard = nowCard
                status = Constants.PLAYROOM_STATUS_JOINER_OK
            }
        }
        updatePlayRoom(playRoom)
    }
    override fun startGame(playRoom: PlayRoom){
        updatePlayRoom(playRoom)
    }
    override fun removeGame(roomIDString: String){
        mRef.child(roomIDString).removeValue()
    }
    override fun joinerLeaveRoom(playRoom: PlayRoom) {
        playRoom.apply {
            joiner = ""
            status = Constants.PLAYROOM_STATUS_WAIT
            joinerPoint = 0
            creatorPoint = 0
        }
        updatePlayRoom(playRoom)
    }




}