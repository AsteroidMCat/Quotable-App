package com.zybooks.quotable.ui.theme

import androidx.compose.ui.Modifier
import java.util.UUID

data class Quote(
    var id: UUID = UUID.randomUUID(),
    val body: String = "",
    val author: String = "",
    val favorite: Boolean = false,
    val modifier: Modifier = Modifier
)
