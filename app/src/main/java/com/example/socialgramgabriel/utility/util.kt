package com.example.socialgramgabriel.utility

import android.content.Context
import android.widget.Toast

fun Context.displayToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}