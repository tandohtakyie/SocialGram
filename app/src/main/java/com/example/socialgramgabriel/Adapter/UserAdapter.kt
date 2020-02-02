package com.example.socialgramgabriel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialgramgabriel.Fragments.HomeFragment
import com.example.socialgramgabriel.Model.User
import com.example.socialgramgabriel.R
import kotlinx.android.synthetic.main.user_item_layout.view.*
import org.jetbrains.annotations.NotNull

class UserAdapter(
    private var mContext: Context,
    private var mUser: List<User>,
    private var isFragment: Boolean = false
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userNameTextView.text = user.getUsername()
        holder.userFullNameTextView.text = user.getFullName()
    }

    class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var userFullNameTextView: TextView = itemView.findViewById(R.id.user_full_name_search)
        var userProfileImage: TextView = itemView.findViewById(R.id.user_profile_image_search)
        var followButton: TextView = itemView.findViewById(R.id.follow_btn_search)
    }
}