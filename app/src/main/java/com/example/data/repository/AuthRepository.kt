package com.example.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.Profile

object AuthRepository {

    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
        
    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, phone: String, password: String, referralCode: String?): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Registration failed: User ID is null.")

            // Create Profile
            val generatedReferral = phone.takeLast(6).ifEmpty { "SB" + (1000..9999).random().toString() }
            val profile = Profile(
                id = userId,
                phone = phone,
                balance = 0.0,
                bonus = 0.0,
                rechargeTotal = 0.0,
                referralCode = generatedReferral,
                referredBy = referralCode,
                teamRank = "VIP0",
                teamSize = 1
            )
            
            // Save Profile in Realtime Database under profiles/$userId
            database.child("profiles").child(userId).setValue(profile).await()

            // Process referral bonus if provided
            if (!referralCode.isNullOrBlank()) {
                try {
                    val dataSnapshot = database.child("profiles")
                        .orderByChild("referralCode")
                        .equalTo(referralCode)
                        .get()
                        .await()
                    
                    val referrerSnapshot = dataSnapshot.children.firstOrNull()
                    if (referrerSnapshot != null) {
                        val referrer = referrerSnapshot.getValue(Profile::class.java)
                        if (referrer != null) {
                            val newTeamSize = referrer.teamSize + 1
                            val newBonus = referrer.bonus + 2.0
                            
                            // Update referrer
                            val referrerId = referrerSnapshot.key ?: ""
                            if (referrerId.isNotEmpty()) {
                                database.child("profiles").child(referrerId)
                                    .updateChildren(
                                        mapOf(
                                            "team_size" to newTeamSize,
                                            "bonus" to newBonus
                                        )
                                    ).await()
                            }
                        }
                    }
                } catch (refEx: Exception) {
                    refEx.printStackTrace() // Log but don't break main flow
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
