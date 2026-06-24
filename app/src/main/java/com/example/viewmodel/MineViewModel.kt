package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.Profile
import com.example.data.model.Order
import com.example.data.model.Transaction
import com.example.data.model.SupportMessage
import com.example.data.model.BankCard
import com.example.data.model.GiftCode
import com.example.data.repository.AuthRepository
import com.example.data.repository.ProfileRepository
import com.example.data.repository.FundRepository
import com.example.data.repository.SupportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MineViewModel : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()

    private val _messages = MutableStateFlow<List<SupportMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _bankCards = MutableStateFlow<List<BankCard>>(emptyList())
    val bankCards = _bankCards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

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

    fun loadOrders() {
        val userId = AuthRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = FundRepository.getOrders(userId)
                _orders.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTransactions() {
        val userId = AuthRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = FundRepository.getTransactions(userId)
                _transactions.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMessages() {
        val userId = AuthRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = SupportRepository.getMessages(userId)
                _messages.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendSupport(messageText: String) {
        val userId = AuthRepository.getCurrentUserId() ?: return
        if (messageText.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                SupportRepository.sendMessage(userId, messageText)
                _message.value = "Message sent successfully"
                loadMessages()
            } catch (e: Exception) {
                _message.value = "Failed to send message: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBankCards() {
        val userId = AuthRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = database.child("bank_cards").get().await()
                val list = snapshot.children.mapNotNull { child ->
                    val card = child.getValue(BankCard::class.java)
                    card?.copy(id = card.id.ifBlank { child.key ?: "" })
                }
                _bankCards.value = list.filter { it.userId == userId }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveBankCard(accountName: String, accountNumber: String, ifscCode: String, bankName: String) {
        val userId = AuthRepository.getCurrentUserId() ?: return
        if (accountName.isBlank() || accountNumber.isBlank() || ifscCode.isBlank() || bankName.isBlank()) {
            _message.value = "All fields are required"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ref = database.child("bank_cards").push()
                val cardId = ref.key ?: ""
                val card = BankCard(
                    id = cardId,
                    userId = userId,
                    accountName = accountName,
                    accountNumber = accountNumber,
                    ifscCode = ifscCode,
                    bankName = bankName
                )
                ref.setValue(card).await()
                _message.value = "Bank card saved successfully"
                loadBankCards()
            } catch (e: Exception) {
                _message.value = "Failed to save bank card: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun redeemGiftCode(code: String) {
        val userId = AuthRepository.getCurrentUserId() ?: return
        val currentProfile = _profile.value ?: return
        if (code.isBlank()) {
            _message.value = "Please enter a code"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = database.child("gift_codes").get().await()
                
                val giftSnapshot = snapshot.children.firstOrNull { child ->
                    val gc = child.getValue(GiftCode::class.java)
                    gc != null && gc.code == code && !gc.isUsed
                }
                val gift = giftSnapshot?.getValue(GiftCode::class.java)
                
                if (giftSnapshot != null && gift != null) {
                    val updatedBalance = currentProfile.balance + gift.amount
                    ProfileRepository.updateBalance(userId, updatedBalance)

                    // Mark gift as used
                    database.child("gift_codes").child(giftSnapshot.key!!)
                        .updateChildren(mapOf(
                            "is_used" to true,
                            "used_by" to userId
                        )).await()

                    // Add gift transaction
                    ProfileRepository.addTransaction(userId, "gift", gift.amount, "Gift Code Redeem: $code")

                    loadProfile()
                    _message.value = "Gift code redeemed! ₹${String.format(Locale.getDefault(), "%.2f", gift.amount)} added."
                } else {
                    _message.value = "Invalid or already used gift code"
                }
            } catch (e: Exception) {
                _message.value = "Redemption error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
