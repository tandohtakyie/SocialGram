package com.example.socialgramgabriel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialgramgabriel.Model.User
import com.example.socialgramgabriel.utility.displayToast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

@Suppress("DEPRECATION")
class AccountSettingsActivity : AppCompatActivity() {
    private lateinit var fireBaseUser: FirebaseUser
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        fireBaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        logout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@AccountSettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        change_image_text_btn.setOnClickListener {
            checker = "clicked"
            CropImage.activity().setAspectRatio(1, 1).start(this@AccountSettingsActivity)
        }

        save_info_profile_btn.setOnClickListener {
            if (checker == "clicked") {
                uploadImageAndUpdateInfo()
            } else {
                updateInfoOnly()
            }
        }

        getUserInfo()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image_view_profile_frag.setImageURI(imageUri)
        } else {

        }
    }

    private fun getUserInfo() {
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(fireBaseUser.uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.defaultprofileimage)
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

    private fun uploadImageAndUpdateInfo() {

        when {
            imageUri == null -> displayToast("Please, provide image!")
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

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Account Settings")
                progressDialog.setMessage("Profile is being updated!")
                progressDialog.show()

                val fileRef = storageProfilePicRef!!.child(fireBaseUser!!.uid + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(
                    OnCompleteListener<Uri> { task ->
                        if (task.isSuccessful) {
                            val downloadUrl = task.result
                            myUrl = downloadUrl.toString()

                            val ref = FirebaseDatabase.getInstance().reference.child("Users")

                            val userMap = HashMap<String, Any>()
                            userMap["fullName"] =
                                full_name_profile_frag.text.toString().toLowerCase()
                            userMap["username"] =
                                username_profile_frag.text.toString().toLowerCase()
                            userMap["bio"] = bio_profile_frag.text.toString().toLowerCase()
                            userMap["image"] = myUrl

                            ref.child(fireBaseUser.uid).updateChildren(userMap)

                            displayToast("Account updated successfully!")

                            val intent =
                                Intent(this@AccountSettingsActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            progressDialog.dismiss()
                        } else {
                            progressDialog.dismiss()
                        }
                    }
                )
            }
        }
    }
}
