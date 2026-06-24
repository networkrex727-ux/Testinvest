package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class FundPlan(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") @PropertyName("name") var name: String = "",
    @get:PropertyName("price") @set:PropertyName("price") @PropertyName("price") var price: Double = 0.0,
    @get:PropertyName("daily_earnings") @set:PropertyName("daily_earnings") @PropertyName("daily_earnings") @SerialName("daily_earnings") var dailyEarnings: Double = 0.0,
    @get:PropertyName("revenue_days") @set:PropertyName("revenue_days") @PropertyName("revenue_days") @SerialName("revenue_days") var revenueDays: Int = 0,
    @get:PropertyName("total_revenue") @set:PropertyName("total_revenue") @PropertyName("total_revenue") @SerialName("total_revenue") var totalRevenue: Double = 0.0,
    @get:PropertyName("fund_type") @set:PropertyName("fund_type") @PropertyName("fund_type") @SerialName("fund_type") var fundType: String = "fixed",
    @get:PropertyName("icon_color") @set:PropertyName("icon_color") @PropertyName("icon_color") @SerialName("icon_color") var iconColor: String = "#6B48D3",
    @get:PropertyName("is_active") @set:PropertyName("is_active") @PropertyName("is_active") @SerialName("is_active") var isActive: Boolean = true
)
