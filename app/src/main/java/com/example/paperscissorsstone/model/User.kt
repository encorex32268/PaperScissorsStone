package com.example.paperscissorsstone.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    var uuid : String,
    var name : String,
    var win : Int ,
    var loss : Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    constructor():this("","",0,0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeString(name)
        parcel.writeInt(win)
        parcel.writeInt(loss)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}