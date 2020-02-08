package com.example.socialgramgabriel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.socialgramgabriel.Model.Post
import com.example.socialgramgabriel.Model.User
import com.example.socialgramgabriel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.posts_layout.view.*

class PostAdapter(
    private val mContext: Context,
    private val mPost: List<Post>
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    private var fireBaseUser: FirebaseUser? = null

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImage: CircleImageView
        var postImage: ImageView
        var likeButton: ImageView
        var commentButton: ImageView
        var saveButton: ImageView
        var username: TextView
        var likes: TextView
        var publisher: TextView
        var description: TextView
        var comments: TextView

        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            postImage = itemView.findViewById(R.id.post_image_home)
            likeButton = itemView.findViewById(R.id.post_image_like_btn)
            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
            saveButton = itemView.findViewById(R.id.post_save_comment_btn)
            username = itemView.findViewById(R.id.user_name_post)
            likes = itemView.findViewById(R.id.likes)
            publisher = itemView.findViewById(R.id.publisher)
            description = itemView.findViewById(R.id.description)
            comments = itemView.findViewById(R.id.comments)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fireBaseUser = FirebaseAuth.getInstance().currentUser
        val post = mPost[position]
        Picasso.get().load(post.getPostImage()).into(holder.postImage)

        publisherInfo(holder.profileImage, holder.username, holder.publisher, post.getPublisher())
    }

    private fun publisherInfo(
        profileImage: CircleImageView,
        username: TextView,
        publisher: TextView,
        publisherID: String
    ) {

        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherID)
            userRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.bean)
                        .into(profileImage)
                    username.text = user.getUsername()
                    publisher.text = user.getFullName()
                }
                override fun onCancelled(p0: DatabaseError) {

                }


            })
    }
}