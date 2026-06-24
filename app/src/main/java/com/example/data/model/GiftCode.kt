package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class GiftCode(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String = "",
    @get:PropertyName("code") @set:PropertyName("code") @PropertyName("code") var code: String = "",
    @get:PropertyName("amount") @set:PropertyName("amount") @PropertyName("amount") var amount: Double = 0.0,
    @get:PropertyName("is_used") @set:PropertyName("is_used") @PropertyName("is_used") @SerialName("is_used") var isUsed: Boolean = false,
    @get:PropertyName("used_by") @set:PropertyName("used_by") @PropertyName("used_by") @SerialName("used_by") var usedBy: String? = null,
    @get:PropertyName("expires_at") @set:PropertyName("expires_at") @PropertyName("expires_at") @SerialName("expires_at") var expiresAt: String? = null
)
