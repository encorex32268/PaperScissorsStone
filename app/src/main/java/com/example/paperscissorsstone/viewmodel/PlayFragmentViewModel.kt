package com.example.paperscissorsstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS

import com.example.paperscissorsstone.model.PlayRoom
import com.google.firebase.database.*

class PlayFragmentViewModel(application: Application)  : AndroidViewModel(application){

    var playRoom = MutableLiveData<PlayRoom>()
    private var mRef : DatabaseReference = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)

    fun getPlayRoomInfo() : LiveData<PlayRoom>{
        return playRoom
    }

    fun listenPlayRoom(roomIDString : String){
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
    }


    fun updatePlayRoom(playRoom: PlayRoom){
        mRef.child(playRoom.id.toString()).setValue(playRoom)
    }





}