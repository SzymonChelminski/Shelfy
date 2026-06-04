package com.example.shelfy.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shelfy.ui.theme.Primary

@Composable
fun ShelfyFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    contentDescription: String = "Add"
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = Primary,
        contentColor = Color.Black,
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp)
        )
    }
}