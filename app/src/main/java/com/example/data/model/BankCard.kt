package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class BankCard(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String = "",
    @get:PropertyName("user_id") @set:PropertyName("user_id") @PropertyName("user_id") @SerialName("user_id") var userId: String = "",
    @get:PropertyName("account_name") @set:PropertyName("account_name") @PropertyName("account_name") @SerialName("account_name") var accountName: String = "",
    @get:PropertyName("account_number") @set:PropertyName("account_number") @PropertyName("account_number") @SerialName("account_number") var accountNumber: String = "",
    @get:PropertyName("ifsc_code") @set:PropertyName("ifsc_code") @PropertyName("ifsc_code") @SerialName("ifsc_code") var ifscCode: String = "",
    @get:PropertyName("bank_name") @set:PropertyName("bank_name") @PropertyName("bank_name") @SerialName("bank_name") var bankName: String = ""
)
