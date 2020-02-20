package com.example.socialgramgabriel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialgramgabriel.Adapter.UserAdapter
import com.example.socialgramgabriel.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class ShowUsersActivity : AppCompatActivity() {

    var id: String = ""
    var title: String = ""

    var userAdapter: UserAdapter? = null
    var userList: List<User>? = null
    var idList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users)

        val intent = intent
        id = intent.getStringExtra("id")
        title = intent.getStringExtra("title")

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = ArrayList()
        userAdapter = UserAdapter(this, userList as ArrayList<User>, false)
        recyclerView.adapter = userAdapter

        idList = ArrayList()
        when (title) {
            "likes" -> getLikes()
            "following" -> getFollowing()
            "followers" -> getFollowers()
            "views" -> getViews()
        }
    }

    private fun getViews() {

    }

    private fun getFollowers() {
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(id!!)
            .child("Followers")

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                (idList as ArrayList<String>).clear()
                for (snapshot in dataSnapshot.children) {
                    (idList as ArrayList<String>).add(snapshot.key!!)
                }

                showUsers()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getFollowing() {
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(id!!)
            .child("Following")

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                (idList as ArrayList<String>).clear()
                for (snapshot in dataSnapshot.children) {
                    (idList as ArrayList<String>).add(snapshot.key!!)
                }

                showUsers()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getLikes() {
        val likeRef = FirebaseDatabase.getInstance().reference
            .child("Likes")
            .child(id!!)

        likeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    (idList as ArrayList<String>).clear()

                    for (snapshot in dataSnapshot.children) {
                        (idList as ArrayList<String>).add(snapshot.key!!)
                    }

                    showUsers()
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }


        })
    }

    private fun showUsers() {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                (userList as ArrayList<User>).clear()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    for (id in idList!!) {
                        if (user!!.getUid() == id) {
                            (userList as ArrayList<User>).add(user!!)
                        }
                    }
                }

                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
