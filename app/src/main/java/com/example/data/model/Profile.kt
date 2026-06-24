package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class Profile(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String = "",
    @get:PropertyName("phone") @set:PropertyName("phone") @PropertyName("phone") var phone: String = "",
    @get:PropertyName("user_serial_id") @set:PropertyName("user_serial_id") @PropertyName("user_serial_id") @SerialName("user_serial_id") var userSerialId: Int? = null,
    @get:PropertyName("balance") @set:PropertyName("balance") @PropertyName("balance") var balance: Double = 0.0,
    @get:PropertyName("bonus") @set:PropertyName("bonus") @PropertyName("bonus") var bonus: Double = 0.0,
    @get:PropertyName("recharge_total") @set:PropertyName("recharge_total") @PropertyName("recharge_total") @SerialName("recharge_total") var rechargeTotal: Double = 0.0,
    @get:PropertyName("referral_code") @set:PropertyName("referral_code") @PropertyName("referral_code") @SerialName("referral_code") var referralCode: String = "",
    @get:PropertyName("referred_by") @set:PropertyName("referred_by") @PropertyName("referred_by") @SerialName("referred_by") var referredBy: String? = null,
    @get:PropertyName("team_rank") @set:PropertyName("team_rank") @PropertyName("team_rank") @SerialName("team_rank") var teamRank: String = "VIP0",
    @get:PropertyName("team_size") @set:PropertyName("team_size") @PropertyName("team_size") @SerialName("team_size") var teamSize: Int = 1
)
