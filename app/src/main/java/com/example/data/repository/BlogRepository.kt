package com.example.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.BlogPost

object BlogRepository {

    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    suspend fun getBlogs(): List<BlogPost> {
        return try {
            val snapshot = database.child("blog_posts").get().await()
            val list = snapshot.children.mapNotNull { child ->
                val post = child.getValue(BlogPost::class.java)
                post?.copy(id = post.id.ifBlank { child.key ?: "" })
            }
            list.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
