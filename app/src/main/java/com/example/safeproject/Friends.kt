package com.example.safeproject

import android.graphics.drawable.Drawable


data class Friend (
    var uid : String? = null,
    val nickname : String? = null,
    val cup : Int? = 0,
    val food : Int? = 0,
    val steps : Int? = 0,
    val score : Double? = 0.0,
    val total_cup : Int? = 0,
    val total_food : Int? = 0,
    val total_steps : Int? = 0,
    val total_score : Double? = 0.0
)