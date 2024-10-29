package com.example.safeproject

import android.app.Dialog
import android.os.Bundle
import com.example.safeproject.databinding.ActivityLoadingBinding

class LoadingActivity(owner : FoodActivity) : Dialog(owner) {

    private lateinit var binding : ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}