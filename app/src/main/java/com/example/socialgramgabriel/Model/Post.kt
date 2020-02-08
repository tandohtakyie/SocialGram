package com.example.socialgramgabriel.Model

class Post {
    private var postID: String = ""
    private var postImage: String = ""
    private var publisher: String = ""
    private var description: String = ""

    constructor()
    constructor(postID: String, postImage: String, publisher: String, description: String) {
        this.postID = postID
        this.postImage = postImage
        this.publisher = publisher
        this.description = description
    }

    fun getPostID(): String {
        return postID
    }

    fun setPostID(postID: String) {
        this.postID = postID
    }

    fun getPostImage(): String {
        return postImage
    }

    fun setPostImage(postImage: String) {
        this.postImage = postImage
    }

    fun getPublisher(): String {
        return publisher
    }

    fun setPublisher(publisher: String) {
        this.publisher = publisher
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }


}