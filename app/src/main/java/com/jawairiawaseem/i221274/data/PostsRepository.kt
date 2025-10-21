package com.jawairiawaseem.i221274.data

import com.google.firebase.database.*
import com.jawairiawaseem.i221274.model.Comment
import com.jawairiawaseem.i221274.model.Post
import java.util.*

object PostsRepository {
    private val db = FirebaseDatabase.getInstance().reference
    private val postsRef = db.child("posts")
    private val likesRef = db.child("postLikes")       // /postLikes/{postId}/{userId}=true
    private val commentsRef = db.child("postComments") // /postComments/{postId}/{commentId}=Comment

    fun createPost(p: Post, onOk: () -> Unit, onErr: (Exception) -> Unit) {
        postsRef.child(p.postId).setValue(p)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { onErr(it) }
    }

    fun listenToPosts(onChange: (List<Post>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {
                val list = s.children.mapNotNull { it.getValue(Post::class.java) }
                    .sortedByDescending { it.createdAt }
                onChange(list)
            }
            override fun onCancelled(e: DatabaseError) {}
        }
        postsRef.addValueEventListener(listener)
        return listener
    }

    fun removePostsListener(listener: ValueEventListener) {
        postsRef.removeEventListener(listener)
    }

    fun isLiked(postId: String, userId: String, cb: (Boolean) -> Unit) {
        likesRef.child(postId).child(userId).get()
            .addOnSuccessListener { cb(it.exists()) }
    }

    fun toggleLike(postId: String, userId: String) {
        val userLikeRef = likesRef.child(postId).child(userId)
        userLikeRef.get().addOnSuccessListener { snap ->
            val liked = snap.exists()
            if (liked) {
                // unlike
                userLikeRef.removeValue()
                postsRef.child(postId).child("likeCount")
                    .runTransaction(object : Transaction.Handler {
                        override fun doTransaction(m: MutableData): Transaction.Result {
                            val v = (m.getValue(Int::class.java) ?: 0) - 1
                            m.value = v.coerceAtLeast(0)
                            return Transaction.success(m)
                        }
                        override fun onComplete(e: DatabaseError?, b: Boolean, s: DataSnapshot?) {}
                    })
            } else {
                // like
                userLikeRef.setValue(true)
                postsRef.child(postId).child("likeCount")
                    .runTransaction(object : Transaction.Handler {
                        override fun doTransaction(m: MutableData): Transaction.Result {
                            val v = (m.getValue(Int::class.java) ?: 0) + 1
                            m.value = v
                            return Transaction.success(m)
                        }
                        override fun onComplete(e: DatabaseError?, b: Boolean, s: DataSnapshot?) {}
                    })
            }
        }
    }

    fun addComment(c: Comment) {
        commentsRef.child(c.postId).child(c.commentId).setValue(c)
        postsRef.child(c.postId).child("commentCount")
            .runTransaction(object : Transaction.Handler {
                override fun doTransaction(m: MutableData): Transaction.Result {
                    val v = (m.getValue(Int::class.java) ?: 0) + 1
                    m.value = v
                    return Transaction.success(m)
                }
                override fun onComplete(e: DatabaseError?, b: Boolean, s: DataSnapshot?) {}
            })
    }

    fun listenToComments(postId: String, onChange: (List<Comment>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {
                val list = s.children.mapNotNull { it.getValue(Comment::class.java) }
                    .sortedBy { it.createdAt }
                onChange(list)
            }
            override fun onCancelled(e: DatabaseError) {}
        }
        commentsRef.child(postId).addValueEventListener(listener)
        return listener
    }

    fun removeCommentsListener(postId: String, l: ValueEventListener) {
        commentsRef.child(postId).removeEventListener(l)
    }
}
