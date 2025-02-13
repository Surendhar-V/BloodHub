package com.example.bloodbank.UI.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bloodbank.R
import com.example.bloodbank.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMainBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bloodbankBtn.setOnClickListener {findNavController().navigate(R.id.action_mainFragment_to_bloodLoginFragment)}

        binding.hospitalBtn.setOnClickListener {
           findNavController().navigate(R.id.action_mainFragment_to_hospitalFragment)
        }

    }

}