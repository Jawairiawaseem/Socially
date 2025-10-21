package com.jawairiawaseem.i221274

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.jawairiawaseem.i221274.data.PostsRepository
import com.jawairiawaseem.i221274.model.Comment
import com.jawairiawaseem.i221274.model.Post
import java.util.*

class CommentsActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var et: EditText
    private lateinit var send: TextView

    private var post: Post? = null
    private lateinit var adapter: CommentAdapter
    private var listener: com.google.firebase.database.ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        post = intent.getSerializableExtra("post") as? Post
        rv = findViewById(R.id.rvComments)
        et = findViewById(R.id.etComment)
        send = findViewById(R.id.btnSend)

        adapter = CommentAdapter(mutableListOf())
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val p = post ?: return
        listener = PostsRepository.listenToComments(p.postId) { list ->
            adapter.submit(list)
            rv.scrollToPosition((list.size - 1).coerceAtLeast(0))
        }

        send.setOnClickListener {
            val text = et.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            val auth = FirebaseAuth.getInstance()
            val c = Comment(
                commentId = UUID.randomUUID().toString(),
                postId = p.postId,
                userId = auth.currentUser?.uid ?: "",
                username = auth.currentUser?.displayName ?: "User",
                text = text,
                createdAt = System.currentTimeMillis()
            )
            PostsRepository.addComment(c)
            et.setText("")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        post?.let { p ->
            listener?.let { PostsRepository.removeCommentsListener(p.postId, it) }
        }
    }
}
