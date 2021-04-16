package com.example.paperscissorsstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.paperscissorsstone.*
import com.example.paperscissorsstone.databinding.FragmentLoginBinding
import com.example.paperscissorsstone.model.User
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val TAG = LoginFragment::class.java.simpleName
    private lateinit var actionBar: ActionBar
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(false)
        binding.apply {
            loginNameEditText.setText(getStringSharedPreferences(Constants.USER_NAME))
            loginButton.setOnClickListener {
                val name = loginNameEditText.text.toString()
                setStringSharedPreferences(Constants.USER_NAME,name)
                var uuid = getStringSharedPreferences(Constants.USER_UUID)
                if (uuid.isNullOrEmpty()){
                    uuid = UUID.randomUUID().toString()
                    setStringSharedPreferences(Constants.USER_UUID,uuid)
                    addUserDataToFirebase(User(uuid, name,0,0))
                }
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                action.username = name
                view.findNavController().navigate(action)
            }
        }

    }
    private fun addUserDataToFirebase(user: User) { getFirebaseDatabaseUsers().child(user.uuid).setValue(user) }


}