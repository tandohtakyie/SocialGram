package com.example.socialgramgabriel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialgramgabriel.Model.User
import com.example.socialgramgabriel.utility.displayToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class AccountSettingsActivity : AppCompatActivity() {
    private lateinit var fireBaseUser: FirebaseUser
    private var checker = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        fireBaseUser = FirebaseAuth.getInstance().currentUser!!

        logout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@AccountSettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        save_info_profile_btn.setOnClickListener {
            if (checker == "clicked") {

            } else {
                updateInfoOnly()
            }
        }

        getUserInfo()
    }

    private fun getUserInfo() {
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(fireBaseUser.uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.bean)
                        .into(profile_image_view_profile_frag)
                    username_profile_frag.setText(user.getUsername())
                    full_name_profile_frag.setText(user.getFullName())
                    bio_profile_frag.setText(user.getBio())
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun updateInfoOnly() {

        when {
            full_name_profile_frag.text.toString() == "" -> {
                displayToast("Please, provide full name!")
            }
            username_profile_frag.text.toString() == "" -> {
                displayToast("Please, provide username!")
            }
            bio_profile_frag.text.toString() == "" -> {
                displayToast("Please, provide bio!")
            }
            else -> {
                val userRef = FirebaseDatabase.getInstance().reference.child("Users")

                val userMap = HashMap<String, Any>()
                userMap["fullName"] = full_name_profile_frag.text.toString().toLowerCase()
                userMap["username"] = username_profile_frag.text.toString().toLowerCase()
                userMap["bio"] = bio_profile_frag.text.toString().toLowerCase()

                userRef.child(fireBaseUser.uid).updateChildren(userMap)
                displayToast("Account updated successfully!")

                val intent = Intent(this@AccountSettingsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }
}
