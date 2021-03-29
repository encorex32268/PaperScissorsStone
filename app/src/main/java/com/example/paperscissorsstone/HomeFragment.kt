package com.example.paperscissorsstone

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paperscissorsstone.databinding.FragmentHomeBinding
import com.example.paperscissorsstone.model.PlayRoom

class HomeFragment : Fragment(R.layout.fragment_home) {

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
                mAdapter = HomeFragmentAdapter(arrayListOf())
                adapter = mAdapter
            }
            viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(HomeFragmentViewModel::class.java)
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




        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }




}