package com.jawairiawaseem.i221274.model

data class Story(
    val storyId: String = "",
    val userId: String = "",
    val username: String = "",
    val mediaType: String = "", // "image" or "video"
    val mime: String = "",      // e.g., "image/jpeg", "video/mp4"
    val base64: String = "",    // encoded media
    val createdAt: Long = 0L    // System.currentTimeMillis()
) : java.io.Serializable
