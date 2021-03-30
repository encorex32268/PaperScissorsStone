package com.example.paperscissorsstone.model

import android.os.Parcel
import android.os.Parcelable

data class PlayRoom(
    val name : String ,
    val id : Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
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