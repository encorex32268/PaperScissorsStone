package com.example.paperscissorsstone.model

import android.os.Parcel
import android.os.Parcelable
import com.example.paperscissorsstone.CardTypes

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
    var joinerPoint : Int

) : Parcelable {




    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    constructor():this("","Wait For Joiner ",0,"",CardTypes.UNKOWN.ordinal,CardTypes.UNKOWN.ordinal,0,0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(creator)
        parcel.writeString(joiner)
        parcel.writeLong(id)
        parcel.writeString(creatorID)
        parcel.writeInt(creatorCard)
        parcel.writeInt(joinerCard)
        parcel.writeInt(creatorPoint)
        parcel.writeInt(joinerPoint)
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
