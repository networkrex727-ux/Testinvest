package com.example.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.FundPlan
import com.example.data.model.Order
import com.example.data.model.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object FundRepository {

    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    suspend fun getFundPlans(fundType: String): List<FundPlan> {
        val remotePlans = try {
            val snapshot = database.child("fund_plans").get().await()
            val list = snapshot.children.mapNotNull { child ->
                val plan = child.getValue(FundPlan::class.java)
                // Ensure id is set from key if blank
                plan?.copy(id = plan.id.ifBlank { child.key ?: "" })
            }
            list.filter { it.fundType == fundType && it.isActive }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
        
        return remotePlans.ifEmpty {
            when (fundType) {
                "fixed" -> listOf(
                    FundPlan("fixed_1", "Starbucks Espresso", 500.0, 45.0, 45, 2025.0, "fixed"),
                    FundPlan("fixed_2", "Starbucks Cold Brew", 2000.0, 210.0, 50, 10500.0, "fixed"),
                    FundPlan("fixed_3", "Starbucks Latte", 5000.0, 550.0, 55, 30250.0, "fixed")
                )
                "welfare" -> listOf(
                    FundPlan("welfare_1", "Welfare Cappuccino", 1000.0, 180.0, 15, 2700.0, "welfare"),
                    FundPlan("welfare_2", "Welfare Mocha", 3000.0, 600.0, 15, 9000.0, "welfare"),
                    FundPlan("welfare_3", "Welfare Macchiato", 8000.0, 1800.0, 15, 27000.0, "welfare")
                )
                "yearly" -> listOf(
                    FundPlan("yearly_1", "Yearly Nitro Special", 10000.0, 400.0, 365, 146000.0, "yearly"),
                    FundPlan("yearly_2", "Yearly Reserve Signature", 30000.0, 1300.0, 365, 474500.0, "yearly")
                )
                else -> emptyList()
            }
        }
    }

    suspend fun buyPlan(userId: String, plan: FundPlan, currentBalance: Double): Result<Unit> {
        return try {
            if (currentBalance < plan.price) {
                return Result.failure(Exception("Insufficient Balance"))
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = Date()
            val startDateStr = sdf.format(today)
            
            val cal = Calendar.getInstance()
            cal.time = today
            cal.add(Calendar.DAY_OF_YEAR, plan.revenueDays)
            val endDateStr = sdf.format(cal.time)

            val sdfFull = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val nowStr = sdfFull.format(today)

            val orderRef = database.child("orders").push()
            val orderId = orderRef.key ?: ""

            // Insert into orders
            val order = Order(
                id = orderId,
                userId = userId,
                planId = plan.id,
                planName = plan.name,
                amountPaid = plan.price,
                dailyEarnings = plan.dailyEarnings,
                revenueDays = plan.revenueDays,
                totalRevenue = plan.totalRevenue,
                startDate = startDateStr,
                endDate = endDateStr,
                status = "active",
                createdAt = nowStr
            )
            orderRef.setValue(order).await()

            // Update profiles balance
            val newBalance = currentBalance - plan.price
            ProfileRepository.updateBalance(userId, newBalance)

            // Insert transaction
            ProfileRepository.addTransaction(
                userId = userId,
                type = "investment",
                amount = plan.price,
                note = "Purchased ${plan.name}"
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrders(userId: String): List<Order> {
        return try {
            val snapshot = database.child("orders").get().await()
            val list = snapshot.children.mapNotNull { child ->
                val ord = child.getValue(Order::class.java)
                ord?.copy(id = ord.id?.ifBlank { child.key } ?: child.key)
            }
            list.filter { it.userId == userId }.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getTransactions(userId: String): List<Transaction> {
        return try {
            val snapshot = database.child("transactions").get().await()
            val list = snapshot.children.mapNotNull { child ->
                val tx = child.getValue(Transaction::class.java)
                tx?.copy(id = tx.id?.ifBlank { child.key } ?: child.key)
            }
            list.filter { it.userId == userId }.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
