package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class BlogPost(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String = "",
    @get:PropertyName("title") @set:PropertyName("title") @PropertyName("title") var title: String = "",
    @get:PropertyName("content") @set:PropertyName("content") @PropertyName("content") var content: String? = null,
    @get:PropertyName("image_url") @set:PropertyName("image_url") @PropertyName("image_url") @SerialName("image_url") var imageUrl: String? = null,
    @get:PropertyName("created_at") @set:PropertyName("created_at") @PropertyName("created_at") @SerialName("created_at") var createdAt: String? = null
)
