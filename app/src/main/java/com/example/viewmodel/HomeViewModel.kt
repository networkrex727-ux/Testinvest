package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FundPlan
import com.example.data.model.Profile
import com.example.data.repository.AuthRepository
import com.example.data.repository.FundRepository
import com.example.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _fundPlans = MutableStateFlow<List<FundPlan>>(emptyList())
    val fundPlans = _fundPlans.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    private val _selectedTab = MutableStateFlow("fixed")
    val selectedTab = _selectedTab.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun loadProfile() {
        val userId = AuthRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            try {
                val prof = ProfileRepository.getProfile(userId)
                _profile.value = prof
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadFundPlans(type: String) {
        _selectedTab.value = type
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val plans = FundRepository.getFundPlans(type)
                _fundPlans.value = plans
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buyPlan(plan: FundPlan) {
        val userId = AuthRepository.getCurrentUserId() ?: return
        val currentProfile = _profile.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val result = FundRepository.buyPlan(userId, plan, currentProfile.balance)
            if (result.isSuccess) {
                _message.value = "Plan purchased successfully"
                loadProfile() // refresh profile balances
            } else {
                _message.value = result.exceptionOrNull()?.message ?: "Purchase failed"
            }
            _isLoading.value = false
        }
    }

    fun addTestDeposit(amount: Double) {
        val userId = AuthRepository.getCurrentUserId() ?: return
        val currentProfile = _profile.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newBalance = currentProfile.balance + amount
                ProfileRepository.updateBalance(userId, newBalance)
                ProfileRepository.addTransaction(
                    userId = userId,
                    type = "recharge",
                    amount = amount,
                    note = "Test Demo Recharge"
                )
                _message.value = "Demo deposit of ₹${String.format(Locale.getDefault(), "%.2f", amount)} completed!"
                loadProfile()
            } catch (e: Exception) {
                _message.value = "Deposit failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTestWithdraw(amount: Double) {
        val userId = AuthRepository.getCurrentUserId() ?: return
        val currentProfile = _profile.value ?: return
        if (currentProfile.balance < amount) {
            _message.value = "Insufficient Balance to withdraw"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newBalance = currentProfile.balance - amount
                ProfileRepository.updateBalance(userId, newBalance)
                ProfileRepository.addTransaction(
                    userId = userId,
                    type = "withdrawal",
                    amount = amount,
                    note = "Test Demo Withdrawal"
                )
                _message.value = "Demo withdrawal of ₹${String.format(Locale.getDefault(), "%.2f", amount)} completed!"
                loadProfile()
            } catch (e: Exception) {
                _message.value = "Withdrawal failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
