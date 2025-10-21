package com.jawairiawaseem.i221274.model

data class Post(
    val postId: String = "",
    val userId: String = "",
    val username: String = "",
    val caption: String = "",
    val imageBase64: String = "",
    val createdAt: Long = 0L,
    val likeCount: Int = 0,
    val commentCount: Int = 0
) : java.io.Serializable
