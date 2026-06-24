package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BlogPost
import com.example.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlogViewModel : ViewModel() {

    private val _blogs = MutableStateFlow<List<BlogPost>>(emptyList())
    val blogs = _blogs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadBlogs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = BlogRepository.getBlogs()
                _blogs.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
