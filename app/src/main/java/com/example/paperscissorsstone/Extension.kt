package com.example.paperscissorsstone

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.paperscissorsstone.Constants.SHAREDPREFERENCE_DATANAME


fun Fragment.setStringSharedPreferences(key : String , value: String ){
    getSharedPreferences(requireContext()).edit().putString(key,value).apply()
}
fun Fragment.getStringSharedPreferences(key : String)= getSharedPreferences(requireContext()).getString(key,"")


//private area

private fun getSharedPreferences(context: Context) = context.getSharedPreferences(SHAREDPREFERENCE_DATANAME,Context.MODE_PRIVATE)
