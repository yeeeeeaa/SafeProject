package com.example.safeproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.safeproject.databinding.FragmentCheckBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class CheckFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var binding : FragmentCheckBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentCheckBinding.inflate(inflater)
        binding.cupBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), CupActivity::class.java))
        }
        binding.foodBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), FoodActivity::class.java))
        }
        binding.stepBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), StepActivity::class.java))
        }
        return binding.root
    }
}