package com.example.socialgramgabriel.Model

class Comment {
    private var comment: String = ""
    private var publisher: String = ""

    constructor()
    constructor(comment: String, publisher: String) {
        this.comment = comment
        this.publisher = publisher
    }


    fun getComment(): String {
        return this.comment
    }

    fun setComment(comment: String) {
        this.comment = comment
    }

    fun getPublisher(): String {
        return this.publisher
    }

    fun setPublisher(publisher: String) {
        this.publisher = publisher
    }
}