package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class Transaction(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String? = null,
    @get:PropertyName("user_id") @set:PropertyName("user_id") @PropertyName("user_id") @SerialName("user_id") var userId: String = "",
    @get:PropertyName("type") @set:PropertyName("type") @PropertyName("type") var type: String = "",
    @get:PropertyName("amount") @set:PropertyName("amount") @PropertyName("amount") var amount: Double = 0.0,
    @get:PropertyName("status") @set:PropertyName("status") @PropertyName("status") var status: String = "pending",
    @get:PropertyName("note") @set:PropertyName("note") @PropertyName("note") var note: String? = null,
    @get:PropertyName("created_at") @set:PropertyName("created_at") @PropertyName("created_at") @SerialName("created_at") var createdAt: String? = null
)
