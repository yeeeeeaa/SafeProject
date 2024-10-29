package com.example.safeproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class FriendsAdapter(private val friend: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_friends_item, parent, false))
    }

    class ViewHolder(friendView: View) : RecyclerView.ViewHolder(friendView) {
        val imageView: ImageView = itemView.findViewById(R.id.home_item_iv)
        val textViewNick : TextView = itemView.findViewById(R.id.friends_item_nickname)
        val textViewScore : TextView = itemView.findViewById(R.id.friends_item_score)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(R.drawable.friends)
            .apply(RequestOptions().circleCrop())
            .into(holder.imageView)
        holder.textViewNick.text = friend[position].nickname
        holder.textViewScore.text = friend[position].score.toString()
    }

    override fun getItemCount(): Int {
        return friend.size
    }
}