package com.example.safeproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safeproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.HomeFragment -> {
                    setFrag(0)
                }
                R.id.FriendsFragment -> {
                    setFrag(1)
                }
                R.id.CheckFragment -> {
                    setFrag(2)
                }
                R.id.InfoFragment -> {
                    setFrag(3)
                }
                else -> {
                    false
                }
            }
        }
        setFrag(0)
    }private fun setFrag(fragNum : Int) : Boolean {
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum){
            0 -> {
                ft.replace(R.id.main_frame, HomeFragment()).commit()
            }
            1 -> {
                ft.replace(R.id.main_frame, FriendsFragment()).commit()
            }
            2 -> {
                ft.replace(R.id.main_frame, CheckFragment()).commit()
            }
            3 -> {
                ft.replace(R.id.main_frame, InfoFragment()).commit()
            }
        }
        return true
    }
}