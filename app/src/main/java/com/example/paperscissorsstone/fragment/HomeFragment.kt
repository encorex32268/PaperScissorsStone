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
import com.example.paperscissorsstone.CardTypes
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_START
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_WAIT
import com.example.paperscissorsstone.Constants.USER_NAME
import com.example.paperscissorsstone.Constants.USER_UUID
import com.example.paperscissorsstone.IHomeItemListener
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentHomeBinding
import com.example.paperscissorsstone.getStringSharedPreferences
import com.example.paperscissorsstone.model.PlayRoom
import com.example.paperscissorsstone.view.HomeFragmentAdapter
import com.example.paperscissorsstone.viewmodel.HomeFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.ThreadLocalRandom

class HomeFragment : Fragment(R.layout.fragment_home), IHomeItemListener {
    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var mAdapter : HomeFragmentAdapter
    private val mRef = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)

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
                mAdapter = HomeFragmentAdapter(arrayListOf())
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
                addPlayRoomToFirebase(view)
            }
        }
    }

    private fun addPlayRoomToFirebase(
        view: View
    ) {
        val playRoom = createPlayRoom()
        mRef.child(playRoom.id.toString())
            .setValue(playRoom)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val action = HomeFragmentDirections.actionHomeFragmentToPlayFragment(playRoom)
                    view.findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        mRef.child(playRoom.id.toString())
            .child("status")
            .setValue(PLAYROOM_STATUS_WAIT)
    }



    private fun createPlayRoom(): PlayRoom {
        return PlayRoom(
            id = generatePlayRoomID(),
            creator = getStringSharedPreferences(USER_NAME)!!,
            joiner = "",
            creatorID = getStringSharedPreferences(USER_UUID)!!,
            creatorCard = CardTypes.UNKOWN.ordinal,
            joinerCard = CardTypes.UNKOWN.ordinal,
            creatorPoint = 0,
            joinerPoint = 0
        )
    }
    private fun generatePlayRoomID(): Long{
        var resultString = ""
        val creatorID = getStringSharedPreferences(USER_UUID)
        if (creatorID != null) {
            for (string in creatorID) {
                val temp: Int = string.toInt()
                resultString += temp
            }
        }
        return resultString.substring(0, 8).toLong()
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
            if (playRoom.creatorID != getStringSharedPreferences(USER_UUID)){
                mRef.child(playRoom.id.toString())
                    .child("status")
                    .setValue(PLAYROOM_STATUS_START)
            }

            val action = HomeFragmentDirections.actionHomeFragmentToPlayFragment(playRoom)
            view?.findNavController()?.navigate(action)
        }
    }




}