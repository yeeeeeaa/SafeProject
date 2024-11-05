package com.example.safeproject

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.safeproject.databinding.ActivityLoadingBinding

class LoadingActivity constructor(context: Context) : Dialog(context) {

    private lateinit var binding : ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setCancelable(false)
    }
}