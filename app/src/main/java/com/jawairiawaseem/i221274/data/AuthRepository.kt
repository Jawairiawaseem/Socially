package com.jawairiawaseem.i221274.data


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jawairiawaseem.i221274.model.UserProfile
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    fun isLoggedIn(): Boolean = auth.currentUser != null
    fun signOut() = auth.signOut()

    suspend fun signUpEmail(email: String, password: String): Result<Unit> = try {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val uid = auth.currentUser!!.uid
        val user = UserProfile(uid = uid, email = email.trim(), profileCompleted = false)
        db.getReference("users").child(uid).setValue(user).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun loginEmail(email: String, password: String): Result<Unit> = try {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
        val uid = auth.currentUser!!.uid
        val ref = db.getReference("users").child(uid)
        val snap = ref.get().await()
        if (!snap.exists()) {
            val user = UserProfile(uid = uid, email = email.trim(), profileCompleted = false)
            ref.setValue(user).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun fetchProfileCompleted(): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val snap = db.getReference("users").child(uid).child("profileCompleted").get().await()
        return snap.getValue(Boolean::class.java) ?: false
    }

    suspend fun markProfileCompleted(fields: Map<String, Any>): Result<Unit> = try {
        val uid = auth.currentUser!!.uid
        val update = HashMap<String, Any>(fields)
        update["profileCompleted"] = true
        db.getReference("users").child(uid).updateChildren(update).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
