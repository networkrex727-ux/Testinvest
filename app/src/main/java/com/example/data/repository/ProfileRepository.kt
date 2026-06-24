package com.example.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.Profile
import com.example.data.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ProfileRepository {

    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    suspend fun getProfile(userId: String): Profile {
        val snapshot = database.child("profiles").child(userId).get().await()
        return snapshot.getValue(Profile::class.java) ?: throw Exception("Profile not found")
    }

    suspend fun updateBalance(userId: String, newBalance: Double) {
        database.child("profiles").child(userId).child("balance").setValue(newBalance).await()
    }

    suspend fun addTransaction(userId: String, type: String, amount: Double, note: String?) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val nowStr = sdf.format(Date())
        
        val transactionRef = database.child("transactions").push()
        val transactionId = transactionRef.key ?: ""
        
        val transaction = Transaction(
            id = transactionId,
            userId = userId,
            type = type,
            amount = amount,
            status = "completed",
            note = note,
            createdAt = nowStr
        )
        transactionRef.setValue(transaction).await()
    }
}
