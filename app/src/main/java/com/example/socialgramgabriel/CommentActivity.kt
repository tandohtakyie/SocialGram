package com.example.socialgramgabriel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialgramgabriel.Adapter.CommentAdapter
import com.example.socialgramgabriel.Model.Comment
import com.example.socialgramgabriel.Model.User
import com.example.socialgramgabriel.utility.displayToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {

    private var postId = ""
    private var publisherId = ""

    private var fireBaseUser: FirebaseUser? = null

    private var commentAdapter: CommentAdapter? = null
    private var commentList: MutableList<Comment>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val intent = intent
        postId = intent.getStringExtra("postId")
        publisherId = intent.getStringExtra("publisherId")

        fireBaseUser = FirebaseAuth.getInstance().currentUser

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view_comments)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        recyclerView.layoutManager = linearLayoutManager

        commentList = ArrayList()
        commentAdapter = CommentAdapter(this, commentList)
        recyclerView.adapter = commentAdapter



        getUserInfo()

        getPostImage()

        readComments()



        post_comment.setOnClickListener {
            if (add_comment!!.text.toString() == "") {
                displayToast("Please, type in comment!!")
            } else {
                addComment()
            }
        }

    }

    private fun addComment() {
        val commentRef =
            FirebaseDatabase.getInstance().reference.child("Comments")
                .child(postId!!)

        val commentsMap = HashMap<String, Any>()

        commentsMap["comment"] = add_comment!!.text.toString()
        commentsMap["publisher"] = fireBaseUser!!.uid

        commentRef.push().setValue(commentsMap)

        add_comment!!.text.clear()

    }

    private fun getUserInfo() {
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(fireBaseUser!!.uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage())
                        .placeholder(R.drawable.defaultprofileimage)
                        .into(profile_image_comment)

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getPostImage() {
        val postRef =
            FirebaseDatabase.getInstance().reference.child("Posts")
                .child(postId).child("postImage")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val image = dataSnapshot.value.toString()
                    Picasso.get().load(image)
                        .placeholder(R.drawable.defaultprofileimage)
                        .into(post_image_comment)

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun readComments() {
        val commentRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(postId)

        commentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    commentList!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val comment = snapshot.getValue(Comment::class.java)
                        commentList!!.add(comment!!)
                    }
                    commentAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })
    }
}
