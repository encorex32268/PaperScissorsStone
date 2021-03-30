package com.example.paperscissorsstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding


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
        }

    }



}