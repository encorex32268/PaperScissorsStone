package com.example.paperscissorsstone.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentLoginBinding
import com.example.paperscissorsstone.model.PlayRoom
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

            loginButton.setOnClickListener {
                val name = loginNameEditText.text.toString()
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                action.username = name
                view.findNavController().navigate(action)
            }

            //Firebase Test

            //Write

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("PlayRooms")
            myRef.setValue(dumpData())

            //Read
//            myRef.addValueEventListener(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(requireContext(),"Error ${error.message}",Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d(TAG, "onDataChange: ${snapshot.childrenCount}")
//                    snapshot.children.forEach {
//                        val playRoom = it.getValue(PlayRoom::class.java)
//                        Log.d(TAG, "onDataChange: $playRoom")
//                    }
//
//                }
//
//            })



        }

    }

    private fun dumpData(): ArrayList<PlayRoom> {
       return arrayListOf<PlayRoom>(
            PlayRoom("Andy","Lee",33941),
            PlayRoom("Eric","Jack",99182),
            PlayRoom("Bob","Carter",51230),
            PlayRoom("David","Frank",762243),
            PlayRoom("Go","Harry",2231123768)
        )

    }


}