package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.data.await
import com.example.data.model.Profile
import com.example.data.repository.AuthRepository
import com.example.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    private val _referralCount = MutableStateFlow(0)
    val referralCount = _referralCount.asStateFlow()

    private val _totalCommission = MutableStateFlow(0.0)
    val totalCommission = _totalCommission.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val database: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    fun loadTaskData() {
        val userId = AuthRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val prof = ProfileRepository.getProfile(userId)
                _profile.value = prof
                _totalCommission.value = prof.bonus

                if (prof.referralCode.isNotEmpty()) {
                    val snapshot = database.child("profiles").get().await()
                    val list = snapshot.children.mapNotNull { it.getValue(Profile::class.java) }
                    val referredList = list.filter { it.referredBy == prof.referralCode }
                    _referralCount.value = referredList.size
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
