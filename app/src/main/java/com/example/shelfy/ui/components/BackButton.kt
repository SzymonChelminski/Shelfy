package com.example.shelfy.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.shelfy.ui.theme.Text as ThemeText

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = ThemeText,
    backgroundColor: Color = Color.Transparent
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.clip(CircleShape),
        colors = IconButtonDefaults.iconButtonColors(containerColor = backgroundColor)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Go back",
            tint = tint
        )
    }
}
