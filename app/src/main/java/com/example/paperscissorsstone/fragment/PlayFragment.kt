package com.example.paperscissorsstone.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_PLAYROOMS
import com.example.paperscissorsstone.Constants.FIREBASEDATEBASE_USERS
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_OK
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_WIN
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_CREATOR_WINGAME
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINER_OK
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINNER_WIN
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_JOINNER_WINGAME
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_START
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_TIE
import com.example.paperscissorsstone.Constants.PLAYROOM_STATUS_WAIT
import com.example.paperscissorsstone.Constants.USER_NAME
import com.example.paperscissorsstone.Constants.USER_UUID
import com.example.paperscissorsstone.R
import com.example.paperscissorsstone.databinding.FragmentPlayBinding
import com.example.paperscissorsstone.getFirebaseDatabasePlayRoom
import com.example.paperscissorsstone.getStringSharedPreferences
import com.example.paperscissorsstone.model.PlayRoom
import com.example.paperscissorsstone.viewmodel.PlayFragmentViewModel
import com.google.firebase.database.FirebaseDatabase

//https://www.vecteezy.com/vector-art/691497-rock-paper-scissors-neon-icons

class PlayFragment : Fragment(R.layout.fragment_play), View.OnClickListener {
    private val TAG = PlayFragment::class.java.simpleName
    private lateinit var binding : FragmentPlayBinding
    private lateinit var  viewModel : PlayFragmentViewModel
    private var isCreator = false
    private lateinit var actionBar: ActionBar
    private lateinit var playRoom: PlayRoom
    private val mRefPlayRoom = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_PLAYROOMS)
    private val mRefUsers = FirebaseDatabase.getInstance().getReference(FIREBASEDATEBASE_USERS)
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
            arguments?.let { it ->
                init(it)
            }
            okButton.setOnClickListener {okButtonClick()}
        }
    }

    private fun okButtonClick() {
        viewModel.changeCard(playRoom,nowCard,isCreator)
    }

    private var observerPlayRoom = Observer<PlayRoom> { vmPlayRoom ->
        binding.apply {
        playRoom = vmPlayRoom
        if (vmPlayRoom.creator.isBlank() && !isCreator){
            leavePlayRoom()
        }else{
                hostPlayNameTextView.text = vmPlayRoom.creator
                joinerPlayNameTextView.text = vmPlayRoom.joiner?:"Wait..."
                hostPointsTextView.text = vmPlayRoom.creatorPoint.toString()
                joinerPointsTextView.text = vmPlayRoom.joinerPoint.toString()
                when {
                playRoom.creatorPoint == 1 -> {
                    playRoomStatus.text = getRoomStatus(PLAYROOM_STATUS_CREATOR_WINGAME)
                }
                playRoom.joinerPoint ==1 -> {
                    playRoomStatus.text = getRoomStatus(PLAYROOM_STATUS_JOINNER_WINGAME)
                }
                else -> {
                    playRoomStatus.text = getRoomStatus(vmPlayRoom.status)
                }
            }
            if (vmPlayRoom.creatorCard!=99 && vmPlayRoom.joinerCard !=99){
                    hostPlayCardImageView.setImageResource(setCardImageByInt(vmPlayRoom.creatorCard))
                    joinerPlayCardImageView.setImageResource(setCardImageByInt(vmPlayRoom.joinerCard))
                    playRoomStatus.text = getRoomStatus(compare(vmPlayRoom.creatorCard,vmPlayRoom.joinerCard))
            }

        }
    }
    }



    private fun FragmentPlayBinding.init(it: Bundle) {
        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        playTime.visibility = View.INVISIBLE
        hostPlayCardImageView.setImageResource(setCardImageByInt(99))
        joinerPlayCardImageView.setImageResource(setCardImageByInt(99))

        playRoom = it.getParcelable("playRoom")!!
        playRoom.let {
            if (getStringSharedPreferences(USER_UUID).equals(it.creatorID)) {
                it.status = PLAYROOM_STATUS_WAIT
                isCreator = true
            } else {
                it.joiner = getStringSharedPreferences(USER_NAME)
                it.status = PLAYROOM_STATUS_START
                joinerPlayNameTextView.text = it.joiner
                isCreator = false
            }
            viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(PlayFragmentViewModel::class.java)
            viewModel.startGame(it)
            viewModel.getPlayRoomInfo(it.id.toString()).observe(requireActivity(), observerPlayRoom)

        }
    }

    private fun cardClickListener(isCardEnabled : Boolean) {
        binding.apply {
            if (isCardEnabled){
                playCardPapper.setOnClickListener(this@PlayFragment)
                playCardScissors.setOnClickListener(this@PlayFragment)
                playCardStone.setOnClickListener(this@PlayFragment)
            }else{
                playCardPapper.setOnClickListener(null)
                playCardScissors.setOnClickListener(null)
                playCardStone.setOnClickListener(null)
            }
        }


    }

    private fun restart() {

        binding.apply {
            if (playRoom.creatorPoint == 1 || playRoom.joinerPoint == 1) {
                return
            }
            playTime.visibility = View.VISIBLE
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                playTime.text = "Next Play ： ${millisUntilFinished/1000}"
            }
            override fun onFinish() {
                playTime.visibility = View.INVISIBLE
                playRoomStatus.text = getRoomStatus(PLAYROOM_STATUS_START)
                hostPlayCardImageView.setImageResource(setCardImageByInt(99))
                joinerPlayCardImageView.setImageResource(setCardImageByInt(99))

            }
        }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leavePlayRoom()
    }

    private fun compare(creatorCard : Int, joinnerCard : Int) : Int {
        var result = 99
        when (creatorCard) {
            5 ->
                result =  when(joinnerCard){
                    2 -> PLAYROOM_STATUS_JOINNER_WIN
                    0 -> PLAYROOM_STATUS_CREATOR_WIN
                    else -> PLAYROOM_STATUS_TIE
                }
            2 ->
                result =  when(joinnerCard){
                    5 -> PLAYROOM_STATUS_CREATOR_WIN
                    0 -> PLAYROOM_STATUS_JOINNER_WIN
                    else -> PLAYROOM_STATUS_TIE
                }
            0 ->
                result = when(joinnerCard){
                    2 -> PLAYROOM_STATUS_CREATOR_WIN
                    5 -> PLAYROOM_STATUS_JOINNER_WIN
                    else -> PLAYROOM_STATUS_TIE
                }
        }
        return result
    }


    private fun getRoomStatus(status: Int): String {
        return  when(status){
            PLAYROOM_STATUS_WAIT ->{ getString(R.string.wait)}
            PLAYROOM_STATUS_START ->{
                cardClickListener(true)
                getString(R.string.pleasePlay)
            }
            PLAYROOM_STATUS_CREATOR_WIN -> {
                viewModel.addPointCreator(playRoom)
                restart()
                if (isCreator) getString(R.string.win) else getString(R.string.loss)
            }
            PLAYROOM_STATUS_JOINNER_WIN ->{
                viewModel.addPointJoiner(playRoom)
                restart()
                if (isCreator){ getString(R.string.loss) } else getString(R.string.win) }

            PLAYROOM_STATUS_CREATOR_OK->{ if (isCreator){ getString(R.string.wait_for_matcher)} else getString(R.string.matcher_done)}
            PLAYROOM_STATUS_JOINER_OK->{ if (isCreator)getString(R.string.matcher_done) else { getString(R.string.wait_for_matcher) }}
            PLAYROOM_STATUS_TIE ->{
                viewModel.resetCard(playRoom)
                restart()
                getString(R.string.game_tie)
            }
            PLAYROOM_STATUS_CREATOR_WINGAME->{
                viewModel.updateStatus(playRoom)
                if (isCreator){
                getString(R.string.gameover_win)

            } else
                getString(R.string.gameover_loss)
            }
            PLAYROOM_STATUS_JOINNER_WINGAME->{
                viewModel.updateStatus(playRoom)
                if (isCreator){
                getString(R.string.gameover_loss)
            }
            else
                getString(R.string.gameover_win)}
            else->getString(R.string.error)

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                leavePlayRoom()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun leavePlayRoom(){
        view?.findNavController()?.popBackStack()
        playRoom.let {
            if(isCreator){
                viewModel.removeGame(it.id.toString())
            } else{
                if (it.creatorID.isNotEmpty()){
                    viewModel.joinerLeaveRoom(it)
                    getFirebaseDatabasePlayRoom().child(it.id.toString()).setValue(playRoom)
    //                    val uuid = getStringSharedPreferences(USER_UUID).toString()
    //                    var loss = 0
    //                    mRefUsers.child(uuid).child("loss").addListenerForSingleValueEvent(object : ValueEventListener{
    //                        override fun onCancelled(error: DatabaseError) {}
    //                        override fun onDataChange(snapshot: DataSnapshot) { loss = snapshot.value as Int }
    //                    })
    //                    loss++
    //                    mRefUsers.child(uuid).child("loss").setValue(loss)
    //                    mRefPlayRoom.child(it.toString()).setValue(it)

                }else{
                    viewModel.getPlayRoomInfo(it.id.toString()).removeObserver(observerPlayRoom)
                }

            }
        }

    }

    override fun onClick(p0: View?) {
        val image = if (isCreator) binding.hostPlayCardImageView else binding.joinerPlayCardImageView
        when(p0?.id){
            R.id.playCardPapper->{
                image.setImageResource(R.drawable.icon_play_papper)
                nowCard = 5
            }
            R.id.playCardScissors->{
                image.setImageResource(R.drawable.icon_play_scissors)
                nowCard = 2
            }
            R.id.playCardStone->{
                image.setImageResource(R.drawable.icon_play_stone)
                nowCard = 0
            }
        }
    }

    private fun setCardImageByInt(cardType : Int ) : Int{
        return when(cardType){
            5-> R.drawable.icon_play_papper
            2-> R.drawable.icon_play_scissors
            0->R.drawable.icon_play_stone
            else -> R.drawable.ic_play_unkown
        }
    }


}


