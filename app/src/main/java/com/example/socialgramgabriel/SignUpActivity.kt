package com.example.socialgramgabriel

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.socialgramgabriel.utility.displayToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signIn_link_btn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        signup_btn.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val fullName = fullName_signup.text.toString()
        val username = username_signup.text.toString()
        val email = email_signup.text.toString()
        val password = password_signup.text.toString()

        when {
            TextUtils.isEmpty(fullName) -> displayToast("Full name is required")
            TextUtils.isEmpty(username) -> displayToast("Username is required")
            TextUtils.isEmpty(email) -> displayToast("Email is required")
            TextUtils.isEmpty(password) -> displayToast("Password is required")

            else -> {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                val progressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("Sign up")
                progressDialog.setMessage("Please wait, this may take a while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserInfo(fullName, username, email, progressDialog)
                        } else {
                            val message = task.exception!!.toString()
                            displayToast("Error: $message")
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }

    private fun saveUserInfo(
        fullName: String,
        username: String,
        email: String,
        progressDialog: ProgressDialog
    ) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullName"] = fullName.toLowerCase()
        userMap["username"] = username.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "Hey! welcome pal, feel free to edit me"
        userMap["image"] =
            "https://firebasestorage.googleapis.com/v0/b/socialgram-e1619.appspot.com/o/Default%20Images%2Fdefault.jpg?alt=media&token=0d70a33d-09a4-4ce6-8eec-95396019c0bb"

        userRef.child(currentUserID).setValue(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog.dismiss()
                displayToast("Account created successfully!")

                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                val message = task.exception!!.toString()
                displayToast("Error: $message")
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()
            }
        }
    }
}
