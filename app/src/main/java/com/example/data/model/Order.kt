package com.example.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.firebase.database.PropertyName

@Serializable
data class Order(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id") var id: String? = null,
    @get:PropertyName("user_id") @set:PropertyName("user_id") @PropertyName("user_id") @SerialName("user_id") var userId: String = "",
    @get:PropertyName("plan_id") @set:PropertyName("plan_id") @PropertyName("plan_id") @SerialName("plan_id") var planId: String = "",
    @get:PropertyName("plan_name") @set:PropertyName("plan_name") @PropertyName("plan_name") @SerialName("plan_name") var planName: String = "",
    @get:PropertyName("amount_paid") @set:PropertyName("amount_paid") @PropertyName("amount_paid") @SerialName("amount_paid") var amountPaid: Double = 0.0,
    @get:PropertyName("daily_earnings") @set:PropertyName("daily_earnings") @PropertyName("daily_earnings") @SerialName("daily_earnings") var dailyEarnings: Double = 0.0,
    @get:PropertyName("revenue_days") @set:PropertyName("revenue_days") @PropertyName("revenue_days") @SerialName("revenue_days") var revenueDays: Int = 0,
    @get:PropertyName("total_revenue") @set:PropertyName("total_revenue") @PropertyName("total_revenue") @SerialName("total_revenue") var totalRevenue: Double = 0.0,
    @get:PropertyName("start_date") @set:PropertyName("start_date") @PropertyName("start_date") @SerialName("start_date") var startDate: String = "",
    @get:PropertyName("end_date") @set:PropertyName("end_date") @PropertyName("end_date") @SerialName("end_date") var endDate: String? = null,
    @get:PropertyName("status") @set:PropertyName("status") @PropertyName("status") var status: String = "active",
    @get:PropertyName("created_at") @set:PropertyName("created_at") @PropertyName("created_at") @SerialName("created_at") var createdAt: String? = null
)
