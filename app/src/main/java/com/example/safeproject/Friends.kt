package com.example.safeproject

import android.graphics.drawable.Drawable


data class Friend (
    var uid : String? = null,
    val nickname : String? = null,
    val cup : Int? = 0,
    val food : Int? = 0,
    val steps : Int? = 0,
    val score : Int? = 0
)