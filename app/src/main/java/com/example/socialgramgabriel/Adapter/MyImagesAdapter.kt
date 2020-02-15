package com.example.socialgramgabriel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.socialgramgabriel.Fragments.PostDetailsFragment
import com.example.socialgramgabriel.Model.Post
import com.example.socialgramgabriel.R
import com.squareup.picasso.Picasso
import org.jetbrains.annotations.NotNull

class MyImagesAdapter(
    private val mContext: Context,
    mPost: List<Post>
) : RecyclerView.Adapter<MyImagesAdapter.ViewHolder?>() {

    private var mPost: List<Post>? = null

    init {
        this.mPost = mPost
    }

    inner class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var postImage: ImageView

        init {
            postImage = itemView.findViewById(R.id.post_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.images_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: Post = mPost!![position]
        Picasso.get().load(post.getPostImage()).into(holder.postImage)

        holder.postImage.setOnClickListener {
            val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("postId", post.getPostID())
            editor.apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PostDetailsFragment()).commit()
        }
    }
}