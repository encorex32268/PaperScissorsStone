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
import com.google.firebase.database.*

class PlayFragmentViewModel(application: Application)  : AndroidViewModel(application){

    var playRoom = MutableLiveData<PlayRoom>()
    var status = MutableLiveData<Int>()
    private var mRef : DatabaseReference = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)

    fun getPlayRoomInfo(roomIDString : String){
        mRef.child(roomIDString)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playroom = snapshot.getValue(PlayRoom::class.java)
                    if (playroom == null){
                        playRoom.postValue(PlayRoom())
                    }else{
                        playRoom.postValue(playroom!!)
                    }
                }

            })
        mRef.child(roomIDString)
            .child("status")
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) return
                    val resultStatus = snapshot.getValue(Int::class.java)
                    status.postValue(resultStatus!!)

                }

            })
    }

    fun updatePlayRoom(playRoom: PlayRoom){
        mRef.child(playRoom.id.toString()).setValue(playRoom)
    }





}