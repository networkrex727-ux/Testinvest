package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class SupportMessage(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String = "",
    @get:PropertyName("user_id") @set:PropertyName("user_id") @PropertyName("user_id") @SerialName("user_id") var userId: String = "",
    @get:PropertyName("message") @set:PropertyName("message") @PropertyName("message") var message: String = "",
    @get:PropertyName("reply") @set:PropertyName("reply") @PropertyName("reply") var reply: String? = null,
    @get:PropertyName("status") @set:PropertyName("status") @PropertyName("status") var status: String = "open",
    @get:PropertyName("created_at") @set:PropertyName("created_at") @PropertyName("created_at") @SerialName("created_at") var createdAt: String = ""
)
