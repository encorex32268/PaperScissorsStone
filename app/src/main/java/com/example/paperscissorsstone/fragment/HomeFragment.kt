package com.example.paperscissorsstone.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AlertDialogLayout
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paperscissorsstone.Constants.USER_NAME
import com.example.paperscissorsstone.IHomeItemListener
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentHomeBinding
import com.example.paperscissorsstone.getStringSharedPreferences
import com.example.paperscissorsstone.model.PlayRoom
import com.example.paperscissorsstone.view.HomeFragmentAdapter
import com.example.paperscissorsstone.viewmodel.HomeFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.ThreadLocalRandom

class HomeFragment : Fragment(R.layout.fragment_home), IHomeItemListener {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var mAdapter : HomeFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            arguments?.let {
                val name = it.getString("username","unkown")
                homeUserName.text = name
            }
            homeRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                mAdapter =
                    HomeFragmentAdapter(
                        arrayListOf()
                    )
                mAdapter.iHomeItemListener = this@HomeFragment
                adapter = mAdapter
            }
            viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
                HomeFragmentViewModel::class.java)
            viewModel.getAllPlayRooms().observe(requireActivity(), Observer<List<PlayRoom>> {playRooms ->
                mAdapter.apply {
                    this.playRooms = playRooms
                    notifyDataSetChanged()
                }
        })

            homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                     viewModel.queryPlayRooms(it)
                    }
                    return false
                }

            })


            homeAddfloatingActionButton.setOnClickListener {
                val playRoom = PlayRoom(
                    id = 0,
                    creator = getStringSharedPreferences(USER_NAME)!!,
                    joiner = ""
                )
                //upload to Firebase
                val database = FirebaseDatabase.getInstance()
                var myRef = database.getReference("PlayRooms")
                val firebaseID = myRef.push().key
                var resultString = ""
                for (string in firebaseID.toString()) {
                    val temp : Int = string.toInt()
                    resultString+=temp
                }
                playRoom.id = resultString.substring(0,8).toLong()
                myRef.database.getReference("PlayRooms").child(playRoom.id.toString())
                    .setValue(playRoom)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val action = HomeFragmentDirections.actionHomeFragmentToPlayFragment(playRoom)
                            view.findNavController().navigate(action)
                        }
                        else{
                            Toast.makeText(requireContext(),it.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }



            }





        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(playRoom: PlayRoom) {
        if(!playRoom.joiner.isNullOrEmpty()){
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setTitle("Warring")
                setMessage("Can't go to this room is full")
                setPositiveButton("ok",null)
            }.show()
        }else{
            //TODO to change CreatorFragment
            val action = HomeFragmentDirections.actionHomeFragmentToPlayFragment(playRoom)
            view?.findNavController()?.navigate(action)
        }

//        action.username = name
//        view.findNavController().navigate(action)
//
    }




}