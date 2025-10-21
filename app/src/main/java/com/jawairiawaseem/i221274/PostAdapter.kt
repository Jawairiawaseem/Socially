package com.jawairiawaseem.i221274

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jawairiawaseem.i221274.model.Post
import com.jawairiawaseem.i221274.data.MediaEncoding

class PostAdapter(
    private val items: MutableList<Post>,
    private val onLike: (Post) -> Unit,
    private val onComment: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgPost)
        val username: TextView = v.findViewById(R.id.tvUsername)
        val likeBtn: ImageView = v.findViewById(R.id.btnLike)
        val commentBtn: ImageView = v.findViewById(R.id.btnComment)
        val likes: TextView = v.findViewById(R.id.tvLikes)
        val comments: TextView = v.findViewById(R.id.tvComments)
        val caption: TextView = v.findViewById(R.id.tvCaption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val p = items[pos]
        h.username.text = p.username
        h.caption.text = p.caption
        h.likes.text = "${p.likeCount} likes"
        h.comments.text = "   ${p.commentCount} comments"
        MediaEncoding.bitmapFromBase64(p.imageBase64)?.let { h.img.setImageBitmap(it) }

        h.likeBtn.setOnClickListener { onLike(p) }
        h.commentBtn.setOnClickListener { onComment(p) }
    }

    override fun getItemCount() = items.size

    fun submit(list: List<Post>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }
}
