package com.example.paperscissorsstone.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.paperscissorsstone.Constants
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentLoginBinding
import com.example.paperscissorsstone.getStringSharedPreferences
import com.example.paperscissorsstone.model.PlayRoom
import com.example.paperscissorsstone.setStringSharedPreferences
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val TAG = LoginFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loginNameEditText.setText(getStringSharedPreferences(Constants.USER_NAME))
            loginButton.setOnClickListener {
                if (getStringSharedPreferences(Constants.USER_UUID).isNullOrEmpty()){
                    val uuid = UUID.randomUUID().toString()
                    setStringSharedPreferences(Constants.USER_UUID,uuid)
                    Log.d(TAG, "onViewCreated: get new uuid $uuid")
                }else{
                    Log.d(TAG, "onViewCreated: Already have ")
                }
                val name = loginNameEditText.text.toString()
                setStringSharedPreferences(Constants.USER_NAME,name)
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                action.username = name
                view.findNavController().navigate(action)
            }

            //Firebase Test

            //Write

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("PlayRooms")
            myRef.setValue(dumpData())

        }

    }

    private fun dumpData(): ArrayList<PlayRoom> {
       return arrayListOf<PlayRoom>(
            PlayRoom("Andy Empty","",33941),
            PlayRoom("Eric Empty","",99182),
            PlayRoom("Bob","Cater",51230),
            PlayRoom("David","Frank",762243),
            PlayRoom("Go","Harry",2231123768)
        )

    }


}