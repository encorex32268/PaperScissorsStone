package com.example.paperscissorsstone.model

import android.os.Parcel
import android.os.Parcelable

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
    val id : Int
) : Parcelable {
    constructor():this("","",1)

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(creator)
        parcel.writeString(joiner)
        parcel.writeInt(id)
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