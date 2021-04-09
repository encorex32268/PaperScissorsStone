package com.example.paperscissorsstone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paperscissorsstone.*
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_OK
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_WIN
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINER_OK
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINER_WIN
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_SHOW
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_START
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_WAIT
import com.example.paperscissorsstone.Constants.USER_NAME
import com.example.paperscissorsstone.Constants.USER_UUID
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentPlayBinding
import com.example.paperscissorsstone.model.PlayRoom
import com.example.paperscissorsstone.view.PlayFragmentAdapter
import com.example.paperscissorsstone.viewmodel.PlayFragmentViewModel
import com.google.firebase.database.*

//https://www.vecteezy.com/vector-art/691497-rock-paper-scissors-neon-icons

class PlayFragment : Fragment(R.layout.fragment_play) {
    private val TAG = PlayFragment::class.java.simpleName
    private lateinit var binding : FragmentPlayBinding
    private lateinit var mAdapter : PlayFragmentAdapter
    private lateinit var nowCardTypes: CardTypes
    private lateinit var  viewModel : PlayFragmentViewModel
    private var ISCreator = false
    private lateinit var actionBar: ActionBar
    private var playRoom: PlayRoom? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentPlayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
                PlayFragmentViewModel::class.java)
            actionBar = (activity as AppCompatActivity).supportActionBar!!
            actionBar.setDisplayHomeAsUpEnabled(true)

            arguments?.let { it ->

                playRoom = it.getParcelable<PlayRoom>("playRoom")
                playRoom?.let{
                    if (getStringSharedPreferences(USER_UUID).equals(it.creatorID)){
                        joinerPlayNameTextView.text = "Wait for Joiner"
                        ISCreator = true
                    }else{
                        it.joiner = getStringSharedPreferences(USER_NAME)
                        joinerPlayNameTextView.text = it.joiner
                        viewModel.updatePlayRoom(it)
                        ISCreator = false
                    }
                    viewModel.getPlayRoomInfo(it.id.toString())
                    viewModel.playRoom.observe(requireActivity(), Observer { playRoom->
                        hostPlayNameTextView.text = playRoom.creator
                        joinerPlayNameTextView.text = playRoom.joiner?:"Wait..."
                        hostPointsTextView.text = playRoom.creatorPoint.toString()
                        joinerPointsTextView.text = playRoom.joinerPoint.toString()
                        hostPlayCardImageView.setImageResource(getCardResources(playRoom.creatorCard))
                        joinerPlayCardImageView.setImageResource(getCardResources(playRoom.joinerCard))

                    })
                    viewModel.status.observe(requireActivity(), Observer { status ->
                        when(status){
                            PLAYROOM_STATUS_WAIT ->{
                                playRoomStatus.text = if (ISCreator){
                                    "Wait For Joiner"
                                }else{
                                    "等待對方出牌"
                                }
                            }
                            PLAYROOM_STATUS_START->{
                                playRoomStatus.text = "請出牌"
                            }
                            PLAYROOM_STATUS_SHOW->{
                                playRoomStatus.text = "開牌"
                            }
                            PLAYROOM_STATUS_CREATOR_WIN->{
                                playRoomStatus.text = if (ISCreator){
                                    "贏"
                                }else{
                                    "輸"
                                }
                            }
                            PLAYROOM_STATUS_JOINER_WIN->{
                                playRoomStatus.text = if (ISCreator){
                                    "輸"
                                }else{
                                    "贏"
                                }
                            }
                            PLAYROOM_STATUS_CREATOR_OK->{
                                playRoomStatus.text = "參加者思考中"
                            }
                            PLAYROOM_STATUS_JOINER_OK->{
                                playRoomStatus.text = "主持者思考中"
                            }

                        }

                    })
                }
                mAdapter = PlayFragmentAdapter(arrayListOf(
                    CardTypes.STONE,CardTypes.STONE,CardTypes.SCISSORS,CardTypes.PAPER,CardTypes.PAPER
                ))
                mAdapter.iPlayItemListener = object : IPlayItemListener{
                    override fun onItemClick(cardTypes: CardTypes) {
//                        nowCardTypes = cardTypes
//                        when(cardTypes){
//                            CardTypes.PAPER ->hostPlayCardImageView.setImageResource(R.drawable.icon_play_papper)
//                            CardTypes.SCISSORS->hostPlayCardImageView.setImageResource(R.drawable.icon_play_scissors)
//                            CardTypes.STONE->hostPlayCardImageView.setImageResource(R.drawable.icon_play_stone)
//                            CardTypes.UNKOWN -> hostPlayCardImageView.setImageResource(R.drawable.ic_play_unkown)
//                        }

                    }

                }
                playRecyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = GridLayoutManager(requireContext(),3)
                    adapter = mAdapter
                }

                okButton.setOnClickListener {
                    mAdapter.cards.remove(nowCardTypes)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun getCardResources(cardInt: Int) : Int{
        return when(cardInt){
            0->R.drawable.icon_play_papper
            1->R.drawable.icon_play_scissors
            2->R.drawable.icon_play_stone
            else -> R.drawable.ic_play_unkown
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        removePlayRoom()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                removePlayRoom()
                view?.findNavController()?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removePlayRoom() {
        if (ISCreator) {
            FirebaseDatabase.getInstance().getReference("PlayRooms")
                .child(playRoom?.id.toString())
                .removeValue()
        }
    }


}