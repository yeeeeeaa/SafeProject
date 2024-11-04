package com.example.safeproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.safeproject.databinding.FragmentHomeBinding
import com.example.safeproject.databinding.FragmentInfoBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    private var stepCount = 0
    private var cupCount  = 0
    private var foodCount = 0
    private var scoreCount = 0

    private lateinit var stepCountView: TextView
    private lateinit var cupCountView: TextView
    private lateinit var foodCountView: TextView

    private lateinit var database: FirebaseDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentHomeBinding.inflate(inflater)

        stepCountView = binding.stepCountView
        cupCountView = binding.cupCountView
        foodCountView = binding.foodCountView

        database = FirebaseDatabase.getInstance()
        val uid = Firebase.auth.currentUser?.uid.toString()
        var random_id = ""
        database.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val item = data.getValue(Friend::class.java)
                    // UID 값을 변수에 저장
                    if (item?.uid.equals(uid)) {
                        random_id = data.key.toString()
                        if (item != null) {
                            stepCount = item.steps!!
                            cupCount = item.cup!!
                            foodCount = item.food!!

                            stepCountView.text = "$stepCount"
                            cupCountView.text = "$cupCount"
                            foodCountView.text = "$foodCount"


                        }
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
                // 데이터를 읽는 데 실패할 때 호출됩니다.
            }
        })

        return binding.root
    }
}