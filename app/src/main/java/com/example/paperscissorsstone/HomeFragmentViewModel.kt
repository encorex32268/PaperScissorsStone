package com.example.paperscissorsstone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paperscissorsstone.model.PlayRoom

class HomeFragmentViewModel : ViewModel(){

    private lateinit var playRooms : MutableLiveData<List<PlayRoom>>
    init {
        loadPlayRooms()
    }

    fun getAllPlayRooms() : LiveData<List<PlayRoom>> = playRooms

    private fun loadPlayRooms(){
        playRooms = MutableLiveData()
        playRooms.postValue(dump())
    }

    private fun dump(): ArrayList<PlayRoom> {
        val dumpData = arrayListOf<PlayRoom>()
        for (i in 1..30) {
            dumpData.add(PlayRoom("Room $i",i))
        }
        return dumpData
    }

     fun queryPlayRooms(word : String) {
        val queried = arrayListOf<PlayRoom>()
         dump().forEach {
            val name = it.name
            if (name.contains(word)){
                queried.add(it)
            }
        }
        playRooms.postValue(queried)
    }


}