package com.example.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.SupportMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SupportRepository {

    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    suspend fun sendMessage(userId: String, messageStr: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val nowStr = sdf.format(Date())

        val ref = database.child("support_messages").push()
        val msgId = ref.key ?: ""
        val msg = SupportMessage(
            id = msgId,
            userId = userId,
            message = messageStr,
            reply = null,
            status = "open",
            createdAt = nowStr
        )
        ref.setValue(msg).await()
    }

    suspend fun getMessages(userId: String): List<SupportMessage> {
        return try {
            val snapshot = database.child("support_messages").get().await()
            val list = snapshot.children.mapNotNull { child ->
                val msg = child.getValue(SupportMessage::class.java)
                msg?.copy(id = msg.id.ifBlank { child.key ?: "" })
            }
            list.filter { it.userId == userId }.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
