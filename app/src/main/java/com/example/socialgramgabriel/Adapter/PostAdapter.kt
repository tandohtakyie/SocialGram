package com.example.socialgramgabriel.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.socialgramgabriel.CommentActivity
import com.example.socialgramgabriel.MainActivity
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

        if (post.getDescription().equals("")) {
            holder.description.visibility = View.GONE
        } else {
            holder.description.visibility = View.VISIBLE
            holder.description.text = post.getDescription()
        }

        publisherInfo(holder.profileImage, holder.username, holder.publisher, post.getPublisher())

        isLikes(post.getPostID(), holder.likeButton)

        numberOfLikes(holder.likes, post.getPostID())

        getTotalComments(holder.comments, post.getPostID())

        checkSavedStatus(post.getPostID(), holder.saveButton)

        holder.likeButton.setOnClickListener {
            if (holder.likeButton.tag == "Like") {
                FirebaseDatabase.getInstance().reference
                    .child("Likes")
                    .child(post.getPostID())
                    .child(fireBaseUser!!.uid)
                    .setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference
                    .child("Likes")
                    .child(post.getPostID())
                    .child(fireBaseUser!!.uid)
                    .removeValue()

//                val intent = Intent(mContext, MainActivity::class.java)
//                mContext.startActivity(intent)
            }
        }

        holder.commentButton.setOnClickListener {
            val intentComment = Intent(mContext, CommentActivity::class.java)
            intentComment.putExtra("postId", post.getPostID())
            intentComment.putExtra("publisherId", post.getPublisher())
            mContext.startActivity(intentComment)
        }

        holder.comments.setOnClickListener {
            val intentComment = Intent(mContext, CommentActivity::class.java)
            intentComment.putExtra("postId", post.getPostID())
            intentComment.putExtra("publisherId", post.getPublisher())
            mContext.startActivity(intentComment)
        }

        holder.saveButton.setOnClickListener {
            if (holder.saveButton.tag == "Save") {
                FirebaseDatabase.getInstance().reference
                    .child("Saves")
                    .child(fireBaseUser!!.uid)
                    .child(post.getPostID())
                    .setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference
                    .child("Saves")
                    .child(fireBaseUser!!.uid)
                    .child(post.getPostID())
                    .removeValue()
            }
        }
    }

    private fun numberOfLikes(likes: TextView, postID: String) {
        val likeRef = FirebaseDatabase.getInstance().reference
            .child("Likes")
            .child(postID)

        likeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    likes.text = dataSnapshot.childrenCount.toString() + " likes"
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }


        })
    }

    private fun getTotalComments(comments: TextView, postID: String) {
        val commentRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(postID)

        commentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    comments.text =
                        "View all " + dataSnapshot.childrenCount.toString() + " comments"
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }


        })
    }

    private fun isLikes(postID: String, likeButton: ImageView) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val likeRef = FirebaseDatabase.getInstance().reference
            .child("Likes")
            .child(postID)

        likeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(firebaseUser!!.uid).exists()) {
                    likeButton.setImageResource(R.drawable.ic_favorite_red_liked_24dp)
                    likeButton.tag = "Liked"
                } else {
                    likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    likeButton.tag = "Like"
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }


        })
    }

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


    private fun publisherInfo(
        profileImage: CircleImageView,
        username: TextView,
        publisher: TextView,
        publisherID: String
    ) {

        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherID)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage())
                        .placeholder(R.drawable.defaultprofileimage)
                        .into(profileImage)
                    username.text = user!!.getUsername()
                    publisher.text = user!!.getFullName()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })
    }

    private fun checkSavedStatus(postID: String, imageView: ImageView) {
        val savesRef =
            FirebaseDatabase.getInstance().reference.child("Saves").child(fireBaseUser!!.uid)
        savesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(postID).exists()) {
                    imageView.setImageResource(R.drawable.ic_bookmark_black_24dp)
                    imageView.tag = "Saved"
                } else {
                    imageView.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
                    imageView.tag = "Save"
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}