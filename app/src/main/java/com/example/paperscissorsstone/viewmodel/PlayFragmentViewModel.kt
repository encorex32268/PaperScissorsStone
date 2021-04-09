package com.example.paperscissorsstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_OK
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_WIN
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINER_OK
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINER_WIN
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_SHOW
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_START
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_WAIT
import com.example.paperscissorsstone.model.PlayRoom
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlayFragmentViewModel(application: Application)  : AndroidViewModel(application){

    var playRoom = MutableLiveData<PlayRoom>()
    var status = MutableLiveData<Int>()
    fun getPlayRoomInfo(roomIDString : String){
        FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS).child(roomIDString)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playroom = snapshot.getValue(PlayRoom::class.java)
                    playroom?.let {
                        playRoom.postValue(it)
                    }
                }

            })
        FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS).child(roomIDString)
            .child("status")
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("PlayFragmentViewModel", "onDataChange: ${snapshot.value}")
                    val mStatus = when(snapshot.value){
                        PLAYROOM_STATUS_WAIT ->0
                        PLAYROOM_STATUS_START ->1
                        PLAYROOM_STATUS_SHOW ->2
                        PLAYROOM_STATUS_CREATOR_WIN->3
                        PLAYROOM_STATUS_JOINER_WIN ->4
                        PLAYROOM_STATUS_CREATOR_OK->5
                        PLAYROOM_STATUS_JOINER_OK->6
                        else ->99
                    }
                    Log.d("PlayFragmentViewModel", "onDataChange: $mStatus ")
                    status.postValue(mStatus)

                }

            })
    }

    fun updatePlayRoom(playRoom: PlayRoom){
        FirebaseDatabase.getInstance().getReference("PlayRooms").child(playRoom.id.toString())
            .setValue(playRoom)
    }





}