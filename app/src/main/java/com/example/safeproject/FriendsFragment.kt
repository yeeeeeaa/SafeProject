package com.example.safeproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    //private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var friend: ArrayList<Friend>
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.friends_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        friend = arrayListOf()
        database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users") // "user123" child 노드 지정
        val uid = Firebase.auth.currentUser?.uid.toString()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val item = data.getValue(Friend::class.java)
                    // UID 값을 변수에 저장
                    if (item?.uid.equals(uid)) { continue }
                    friend.add(item!!)
                }
                println(friend[0].nickname)
                val friendsAdapter = FriendsAdapter(friend)
                recyclerView.adapter = friendsAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                // 데이터를 읽는 데 실패할 때 호출됩니다.
            }
        })



        return view
    }
}

