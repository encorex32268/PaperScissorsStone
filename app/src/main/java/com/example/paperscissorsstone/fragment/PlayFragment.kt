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
import com.example.paperscissorsstone.*
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS
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
import com.example.paperscissorsstone.viewmodel.PlayFragmentViewModel
import com.google.firebase.database.*

//https://www.vecteezy.com/vector-art/691497-rock-paper-scissors-neon-icons

class PlayFragment : Fragment(R.layout.fragment_play), View.OnClickListener {
    private val TAG = PlayFragment::class.java.simpleName
    private lateinit var binding : FragmentPlayBinding
    private lateinit var  viewModel : PlayFragmentViewModel
    private var isCreator = false
    private lateinit var actionBar: ActionBar
    private var playRoom: PlayRoom? = null
    private val mRefPlayRoom = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)
    private var nowCard =0

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
                playRoom = it.getParcelable("playRoom")
                playRoom?.let{ it ->
                    if (getStringSharedPreferences(USER_UUID).equals(it.creatorID)){
                        joinerPlayNameTextView.text = "Wait for Joiner"
                        isCreator = true
                    }else{
                        it.joiner = getStringSharedPreferences(USER_NAME)
                        joinerPlayNameTextView.text = it.joiner
                        viewModel.updatePlayRoom(it)
                        isCreator = false
                    }
                    viewModel.getPlayRoomInfo(it.id.toString())
                    viewModel.playRoom.observe(requireActivity(), Observer { vmPlayRoom->
                        if (vmPlayRoom.creator.isBlank() && !isCreator){
                            // if creator is leave
                            playRoom = vmPlayRoom
                            view.findNavController().popBackStack()
                        }else{
                            hostPlayNameTextView.text = vmPlayRoom.creator
                            joinerPlayNameTextView.text = vmPlayRoom.joiner?:"Wait..."
                            hostPointsTextView.text = vmPlayRoom.creatorPoint.toString()
                            joinerPointsTextView.text = vmPlayRoom.joinerPoint.toString()
                            hostPlayCardImageView.setImageResource(vmPlayRoom.creatorCard)
                            joinerPlayCardImageView.setImageResource(vmPlayRoom.joinerCard)
                        }


                    })
                    viewModel.status.observe(requireActivity(), Observer { status ->
                        when(status){
                            PLAYROOM_STATUS_WAIT ->{
                                playRoomStatus.text = if (isCreator){
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
                                playRoomStatus.text = if (isCreator){
                                    "贏"
                                }else{
                                    "輸"
                                }
                            }
                            PLAYROOM_STATUS_JOINER_WIN->{
                                playRoomStatus.text = if (isCreator){
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
                    okButton.setOnClickListener {view ->
                        //upload
                        if (isCreator){
                            it.creatorCard = nowCard
                        }else{
                            it.joinerCard = nowCard
                        }
                        viewModel.updatePlayRoom(it)
                    }
                }



            }

            playCardPapper.setOnClickListener(this@PlayFragment)
            playCardScissors.setOnClickListener(this@PlayFragment)
            playCardStone.setOnClickListener(this@PlayFragment)


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
        if (isCreator) {
            mRefPlayRoom.child(playRoom?.id.toString()).removeValue()
        }else{
            if (!playRoom?.creator.isNullOrBlank()){
                mRefPlayRoom.child(playRoom?.id.toString()).child("joiner").setValue("")
            }
        }
    }

    override fun onClick(p0: View?) {
        val image = if (isCreator) binding.hostPlayCardImageView else binding.joinerPlayCardImageView
        when(p0?.id){
            R.id.playCardPapper->{
                image.setImageResource(R.drawable.icon_play_scissors)
                nowCard = R.drawable.icon_play_scissors
            }
            R.id.playCardScissors->{
                image.setImageResource(R.drawable.icon_play_papper)
                nowCard = R.drawable.icon_play_papper
            }
            R.id.playCardStone->{
                image.setImageResource(R.drawable.icon_play_stone)
                nowCard = R.drawable.icon_play_stone
            }
        }
    }


}


