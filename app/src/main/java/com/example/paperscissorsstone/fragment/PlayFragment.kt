package com.example.paperscissorsstone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paperscissorsstone.*
import com.example.paperscissorsstone.databinding.FragmentPlayBinding
import com.example.paperscissorsstone.model.PlayRoom
import com.example.paperscissorsstone.view.PlayFragmentAdapter

//https://www.vecteezy.com/vector-art/691497-rock-paper-scissors-neon-icons

class PlayFragment : Fragment(R.layout.fragment_play) {

    private lateinit var binding : FragmentPlayBinding
    private lateinit var mAdapter : PlayFragmentAdapter
    private lateinit var nowCardTypes: CardTypes
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            arguments?.let { it ->
                val playRoom = it.getParcelable<PlayRoom>("playRoom")
                playRoom?.let{
                    hostPlayNameTextView.text = getStringSharedPreferences(Constants.USER_NAME)
                    joinerPlayNameTextView.text = it.creator
                    hostPointsTextView.text = "0"
                    joinerPointsTextView.text = "0"
                    hostPlayCardImageView.setImageResource(R.drawable.ic_play_unkown)
                    joinerPlayCardImageView.setImageResource(R.drawable.ic_play_unkown)
                }
                mAdapter = PlayFragmentAdapter(arrayListOf(
                    CardTypes.STONE,CardTypes.STONE,CardTypes.SCISSORS,CardTypes.PAPER,CardTypes.PAPER
                ))
                mAdapter.iPlayItemListener = object : IPlayItemListener{
                    override fun onItemClick(cardTypes: CardTypes) {
                        nowCardTypes = cardTypes
                        when(cardTypes){
                            CardTypes.PAPER ->hostPlayCardImageView.setImageResource(R.drawable.icon_play_papper)
                            CardTypes.SCISSORS->hostPlayCardImageView.setImageResource(R.drawable.icon_play_scissors)
                            CardTypes.STONE->hostPlayCardImageView.setImageResource(R.drawable.icon_play_stone)
                            CardTypes.UNKOWN -> hostPlayCardImageView.setImageResource(R.drawable.ic_play_unkown)
                        }

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



}