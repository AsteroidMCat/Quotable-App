package com.zybooks.quotable.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.zybooks.quotable.R

@Composable
fun AboutScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Quotable!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
        )

        Icon(
            painter = painterResource(id = R.drawable.chat_bubble_svgrepo_com_converted_image),
            contentDescription = "Chat Bubble",
            tint = Color.White,
        )

        Text(
            "by AsteroidMCat",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
        )
    }
}
