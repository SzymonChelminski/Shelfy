package com.example.shelfy.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shelfy.data.local.entity.ScannedProductEntity
import com.example.shelfy.data.repository.ScannedProductRepository
import com.example.shelfy.notifications.ExpiryNotificationWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScannerViewModel(
    application: Application,
    private val repository: ScannedProductRepository
) : AndroidViewModel(application) {

    val savedProducts: StateFlow<List<ScannedProductEntity>> = repository
        .getAllProducts()
        .onEach { products ->
            Log.d("SHELFY_DB", "Saved products (${products.size}):")
            products.forEach { p ->
                Log.d("SHELFY_DB", "  [${p.barcode}] ${p.name} – expires ${p.expiryDateMillis}")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun saveProduct(
        barcode: String,
        name: String,
        brand: String,
        imageUrl: String?,
        nutriscoreGrade: String?,
        expiryDateMillis: Long,
        quantity: String = "",
        category: String = ""
    ) {
        viewModelScope.launch {
            repository.insert(
                ScannedProductEntity(
                    barcode = barcode,
                    name = name,
                    brand = brand,
                    imageUrl = imageUrl,
                    nutriscoreGrade = nutriscoreGrade,
                    expiryDateMillis = expiryDateMillis,
                    quantity = quantity,
                    category = category
                )
            )
            triggerExpiryCheck()
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            val entity = savedProducts.value.firstOrNull { it.id.toInt() == id }
            entity?.let { repository.delete(it) }
        }
    }

    fun updateProduct(id: Int, name: String, brand: String, expiryDateMillis: Long, quantity: String, category: String) {
        viewModelScope.launch {
            val entity = savedProducts.value.firstOrNull { it.id.toInt() == id }
            entity?.let {
                repository.update(it.copy(name = name, brand = brand, expiryDateMillis = expiryDateMillis, quantity = quantity, category = category))
                triggerExpiryCheck()
            }
        }
    }

    // Runs an immediate check instead of waiting for the next daily periodic run,
    // so a product added/edited close to its expiry window is caught right away.
    private fun triggerExpiryCheck() {
        val work = OneTimeWorkRequestBuilder<ExpiryNotificationWorker>().build()
        WorkManager.getInstance(getApplication()).enqueue(work)
    }

    companion object {
        fun factory(application: Application, repository: ScannedProductRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ScannerViewModel(application, repository) as T
            }
    }
}
