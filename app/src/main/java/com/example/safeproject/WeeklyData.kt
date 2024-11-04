package com.example.safeproject

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object WeeklyData {
    private lateinit var database: FirebaseDatabase
    fun updateDatabaseValue() {
        database = FirebaseDatabase.getInstance()
        var random_id = ""
        // 데이터 수정 (예: 특정 키의 값을 업데이트)
        database.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val item = data.getValue(Friend::class.java)
                    // UID 값을 변수에 저장
                    random_id = data.key.toString()
                    item?.let {
                        val updateStep = mapOf(
                            "total_cup" to item.cup!!,
                            "total_steps" to item.steps!!,
                            "total_food" to item.food!!,
                            "total_score" to item.score,
                            "score" to 0.0,
                            "cup" to 0,
                            "steps" to 0,
                            "food" to 0// name 필드만 업데이트
                        )
                        database.getReference("users").child(random_id).updateChildren(updateStep)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    println("끝")
                                } else {
                                    Log.e(
                                        "FirebaseError",
                                        "데이터 업데이트 실패: ${task.exception?.message}"
                                    )
                                }
                            }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // 데이터를 읽는 데 실패할 때 호출됩니다.
            }
        })
    }
}