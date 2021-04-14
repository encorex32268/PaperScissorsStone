package com.example.paperscissorsstone.model

import android.os.Parcel
import android.os.Parcelable
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_WAIT
import com.example.paperscissorsstone.R

/**
 * Data :
 * creator -> game creator
 * id  -> room id
 * joiner  -> gamer
 *
 */


data class PlayRoom(
    val creator : String,
    var joiner : String?,
    var id : Long,
    val creatorID:String,
    var creatorCard : Int,
    var joinerCard : Int,
    var creatorPoint : Int,
    var joinerPoint : Int,
    var status : Int

) : Parcelable {




    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    constructor():this("","",0,"", R.drawable.ic_play_unkown,R.drawable.ic_play_unkown,0,0,PLAYROOM_STATUS_WAIT)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(creator)
        parcel.writeString(joiner)
        parcel.writeLong(id)
        parcel.writeString(creatorID)
        parcel.writeInt(creatorCard)
        parcel.writeInt(joinerCard)
        parcel.writeInt(creatorPoint)
        parcel.writeInt(joinerPoint)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayRoom> {
        override fun createFromParcel(parcel: Parcel): PlayRoom {
            return PlayRoom(parcel)
        }

        override fun newArray(size: Int): Array<PlayRoom?> {
            return arrayOfNulls(size)
        }
    }
}
