package com.example.socialgramgabriel

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.socialgramgabriel.utility.displayToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signup_link_btn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        login_btn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = email_login.text.toString()
        val password = password_login.text.toString()

        when{

            TextUtils.isEmpty(email) -> displayToast("Email is required")
            TextUtils.isEmpty(password) -> displayToast("Password is required")

            else ->{
                val progressDialog = ProgressDialog(this@SignInActivity)
                progressDialog.setTitle("Logging in")
                progressDialog.setMessage("Please wait, this may take a while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        progressDialog.dismiss()

                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else {
                        val message = task.exception!!.toString()
                        displayToast("Error: $message")
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
