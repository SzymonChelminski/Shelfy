package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelfy.model.ShoppingItem
import com.example.shelfy.ui.theme.Text as ThemeText

@Composable
fun ShoppingScreen(modifier: Modifier = Modifier) {
    val items = remember {
        mutableStateListOf<ShoppingItem>().apply { addAll(ShoppingItem.mockShoppingList) }
    }
    var newItemText by remember { mutableStateOf("") }

    val toBuy = items.filter { !it.isCompleted }
    val completed = items.filter { it.isCompleted }

    fun toggle(item: ShoppingItem) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            items[index] = items[index].copy(isCompleted = !items[index].isCompleted)
        }
    }

    fun addItem() {
        val name = newItemText.trim()
        if (name.isEmpty()) return
        val nextId = (items.maxOfOrNull { it.id } ?: 0) + 1
        items.add(ShoppingItem(nextId, name, "Other", "1", false))
        newItemText = ""
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Shopping List",
            color = ThemeText,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        AddItemBar(
            value = newItemText,
            onValueChange = { newItemText = it },
            onAdd = { addItem() }
        )

        if (toBuy.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel(text = "TO BUY")
            Spacer(modifier = Modifier.height(12.dp))
            toBuy.forEach { item ->
                ShoppingListItemCard(
                    item = item,
                    onToggle = { toggle(item) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        if (completed.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel(text = "COMPLETED")
            Spacer(modifier = Modifier.height(12.dp))
            completed.forEach { item ->
                ShoppingListItemCard(
                    item = item,
                    onToggle = { toggle(item) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = ThemeText.copy(alpha = 0.5f),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(start = 4.dp)
    )
}
