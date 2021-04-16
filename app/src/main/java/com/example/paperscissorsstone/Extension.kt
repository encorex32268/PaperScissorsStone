package com.example.paperscissorsstone

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_USERS
import com.example.paperscissorsstone.Constants.SHAREDPREFERENCE_DATANAME
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


fun Fragment.setStringSharedPreferences(key : String , value: String ){
    getSharedPreferences(requireContext()).edit().putString(key,value).apply()
}
fun Fragment.getStringSharedPreferences(key : String)= getSharedPreferences(requireContext()).getString(key,"")


fun Fragment.getFirebaseDatabasePlayRoom():DatabaseReference {
    return FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)
}
fun Fragment.getFirebaseDatabaseUsers():DatabaseReference {
    return FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_USERS)
}


//private

private fun getSharedPreferences(context: Context) = context.getSharedPreferences(SHAREDPREFERENCE_DATANAME,Context.MODE_PRIVATE)
