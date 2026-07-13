package com.example.shelfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
fun ProductImage(
    imageUrl: String?,
    tint: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp = 44.dp,
    contentScale: ContentScale = ContentScale.Crop
) {
    var loadFailed by remember(imageUrl) { mutableStateOf(false) }
    val showPlaceholder = imageUrl.isNullOrEmpty() || loadFailed

    Box(
        modifier = modifier.background(tint.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty() && !loadFailed) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
                onState = { state -> loadFailed = state is AsyncImagePainter.State.Error }
            )
        }
        if (showPlaceholder) {
            Icon(
                imageVector = Icons.Outlined.FoodBank,
                contentDescription = null,
                tint = tint.copy(alpha = 0.4f),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
