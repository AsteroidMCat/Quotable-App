package com.zybooks.quotable.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun QuoteListScreen(
    getFavoriteQuotes: () -> List<Quote>,
    deleteQuote: (Quote) -> Unit,
    toggleFavoriteQuote: (Quote) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Quote List",
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn {
            items(
                items = getFavoriteQuotes(),
                key = { quote: Quote -> quote.id }
            ) { quote ->
                QuoteListItem(
                    modifier = Modifier,
                    quote = quote,
                    onDelete = deleteQuote,
                    toggleFavoriteQuote = toggleFavoriteQuote
                )
            }
        }
    }

}


//code from https://www.geeksforgeeks.org/android-jetpack-compose-swipe-to-dismiss-with-material-3/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteListItem(
    quote: Quote,
    modifier: Modifier = Modifier,
    onDelete: (Quote) -> Unit,
    toggleFavoriteQuote: (Quote) -> Unit,
) {
    val context = LocalContext.current
    val currentItem by rememberUpdatedState(quote)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete(currentItem)
                    Toast.makeText(
                        context, "${quote.author}'s quote deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                SwipeToDismissBoxValue.StartToEnd -> {
                    toggleFavoriteQuote(currentItem)
                    Toast.makeText(
                        context, "Quote added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }

                else -> false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = { QuoteListDismissBackground(dismissState) },
        content = {
            QuoteCard(
                quote = quote,
                onFavoriteClick = { toggleFavoriteQuote(currentItem) }
            )
        }
    )
}

//Code below from https://www.geeksforgeeks.org/android-jetpack-compose-swipe-to-dismiss-with-material-3/; modified for this project
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteListDismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        SwipeToDismissBoxValue.StartToEnd -> Color.Green
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            //Code from Android Studio AI
            imageVector = Icons.Default.Favorite,
            contentDescription = "Add to favorites",
            tint = Color.Black
        )
        Icon(
            //Code from Android Studio AI
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.Black
        )
    }
}
