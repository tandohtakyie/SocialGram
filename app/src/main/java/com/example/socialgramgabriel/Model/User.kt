package com.example.socialgramgabriel.Model

class User(
    private var username: String,
    private var fullName: String,
    private var bio: String,
    private var image: String,
    private var uid: String
) {


    fun getUsername(): String{
        return username
    }

    fun setUsername(username: String){
        this.username = username
    }

    fun getFullName(): String{
        return fullName
    }

    fun setfullName(fullName: String){
        this.fullName = fullName
    }

    fun getBio(): String{
        return bio
    }

    fun setBio(bio: String){
        this.bio = bio
    }

    fun getImage(): String{
        return image
    }

    fun setImage(image: String){
        this.image = image
    }

    fun getUid(): String{
        return uid
    }

    fun setUid(uid: String){
        this.uid = uid
    }
}