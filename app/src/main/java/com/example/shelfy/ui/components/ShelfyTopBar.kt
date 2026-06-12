package com.example.shelfy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.shelfy.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.shelfy.ui.theme.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfyTopBar(onSettingsClick: () -> Unit = {}) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shelfy_icon),
                    contentDescription = "Shelfy Logo",
                    modifier = Modifier
                        .size(72.dp)
                )
                Text(
                    text = "Shelfy",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Text,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings icon",
                    tint = Text,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .size(32.dp)
                        .clickable { onSettingsClick() },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}