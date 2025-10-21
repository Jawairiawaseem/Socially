package com.jawairiawaseem.i221274.model


data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val fullName: String = "",
    val photoUrl: String = "",
    val profileCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
