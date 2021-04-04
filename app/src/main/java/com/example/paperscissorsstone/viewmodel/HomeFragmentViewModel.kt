package com.example.paperscissorsstone.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paperscissorsstone.model.PlayRoom
import com.google.firebase.database.*

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application){

    private lateinit var playRooms : MutableLiveData<List<PlayRoom>>
    private var firebaseDatabase : FirebaseDatabase?=null
    private var myRef : DatabaseReference?=null
    private lateinit var originPlayRooms : ArrayList<PlayRoom>
    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase!!.getReference("PlayRooms")
        loadPlayRooms()
        originPlayRooms = arrayListOf()
    }

    fun getAllPlayRooms() : LiveData<List<PlayRoom>> = playRooms

    private fun loadPlayRooms(){
        playRooms = MutableLiveData()
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val dumpData = arrayListOf<PlayRoom>()
                snapshot.children.forEach {
                    val playRoom = it.getValue(PlayRoom::class.java)
                    dumpData.add(playRoom!!)
                }
                playRooms.postValue(dumpData)
                originPlayRooms = dumpData

            }

        })




    }


     fun queryPlayRooms(id : String) {
        val queried = arrayListOf<PlayRoom>()
         for (playRoom in originPlayRooms) {
             val roomId = playRoom.id.toString()
             if (roomId.contains(id)){
                 queried.add(playRoom)
             }
         }
        playRooms.postValue(queried)
    }


}