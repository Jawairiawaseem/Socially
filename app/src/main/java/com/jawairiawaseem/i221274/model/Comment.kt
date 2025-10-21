package com.jawairiawaseem.i221274.model

data class Comment(
    val commentId: String = "",
    val postId: String = "",
    val userId: String = "",
    val username: String = "",
    val text: String = "",
    val createdAt: Long = 0L
)
