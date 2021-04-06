package com.example.paperscissorsstone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paperscissorsstone.Constants.USER_NAME
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentPlayBinding
import com.example.paperscissorsstone.getStringSharedPreferences

class CreatorFragment : Fragment(R.layout.fragment_play) {

    private lateinit var binding:FragmentPlayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            hostPlayNameTextView.text = getStringSharedPreferences(USER_NAME)

        }
    }



}

