package com.jawairiawaseem.i221274.data

import com.google.firebase.database.FirebaseDatabase
import com.jawairiawaseem.i221274.model.Story

object StoriesRepository {

    private val root = FirebaseDatabase.getInstance().reference.child("stories")
    private const val DAY_MS = 24 * 60 * 60 * 1000L

    fun uploadStory(story: Story, onDone: () -> Unit, onError: (Exception) -> Unit) {
        root.child(story.userId).child(story.storyId)
            .setValue(story)
            .addOnSuccessListener { onDone() }
            .addOnFailureListener { onError(it) }
    }

    fun fetchValidStories(onResult: (List<Story>) -> Unit, onError: (Exception) -> Unit) {
        val now = System.currentTimeMillis()
        root.get()
            .addOnSuccessListener { snap ->
                val list = mutableListOf<Story>()
                for (userNode in snap.children) {
                    for (storyNode in userNode.children) {
                        val s = storyNode.getValue(Story::class.java)
                        if (s != null) {
                            if (now - s.createdAt < DAY_MS) {
                                list.add(s)
                            } else {
                                // client cleanup
                                storyNode.ref.removeValue()
                            }
                        }
                    }
                }
                list.sortByDescending { it.createdAt }
                onResult(list)
            }
            .addOnFailureListener { onError(it) }
    }
}
