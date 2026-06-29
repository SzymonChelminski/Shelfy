package com.example.shelfy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shelfy.data.local.entity.ShoppingItemEntity
import com.example.shelfy.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    val items: StateFlow<List<ShoppingItemEntity>> = repository.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addItem(name: String, category: String) {
        viewModelScope.launch {
            repository.insert(
                ShoppingItemEntity(name = name, category = category, quantity = "1", isCompleted = false)
            )
        }
    }

    fun toggleItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.updateCompleted(item.id, !item.isCompleted)
        }
    }

    fun deleteItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    fun clearCompleted() {
        viewModelScope.launch {
            repository.clearCompleted()
        }
    }

    companion object {
        fun factory(repository: ShoppingRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ShoppingViewModel(repository) as T
            }
    }
}
