package com.zybooks.quotable.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteQuotesScreen(
    getFavoriteQuotes: () -> List<Quote>,
    deleteFromFavorites: (Quote) -> Unit,
    toggleFavoriteQuote: (Quote) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "❤\uFE0F'd Quotes",
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn {
            items(
                items = getFavoriteQuotes(),
                key = { quote: Quote -> quote.id }
            ) { quote ->
                QuoteItem(
                    modifier = Modifier,
                    quote = quote,
                    onDeleteFromFavorites = deleteFromFavorites,
                    toggleFavoriteQuote = toggleFavoriteQuote
                )
            }
        }
    }
}

@Composable
fun QuoteCard(
    quote: Quote,
    onFavoriteClick: (Quote) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(
                text = quote.body,
                modifier = Modifier.align(Alignment.Start)
            )
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text =
                    if (quote.author == "") {
                        "-Unknown"
                    } else {
                        "-${quote.author}"
                    },
                    fontStyle = FontStyle.Italic
                )
                IconButton(onClick = { onFavoriteClick(quote) }) {
                    if (quote.favorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            tint = Color.Red,
                            contentDescription = "Remove from favorites"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Add to favorites"
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun QuoteCardPreview() {
    val q = Quote(body = "Test Quote", author = "Test Author")
    QuoteCard(quote = q, onFavoriteClick = {})
}

//Code below from https://www.geeksforgeeks.org/android-jetpack-compose-swipe-to-dismiss-with-material-3/; modified for this project
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteQuotesDismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            //Code from Android Studio AI
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.Black
        )
    }
}

//code from https://www.geeksforgeeks.org/android-jetpack-compose-swipe-to-dismiss-with-material-3/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteItem(
    quote: Quote,
    modifier: Modifier = Modifier,
    onDeleteFromFavorites: (Quote) -> Unit,
    toggleFavoriteQuote: (Quote) -> Unit,
) {
    val context = LocalContext.current
    val currentItem by rememberUpdatedState(quote)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteFromFavorites(currentItem)
                    toggleFavoriteQuote(currentItem)
                    Toast.makeText(
                        context, "${quote.author}'s quote deleted from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                else -> false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = { FavoriteQuotesDismissBackground(dismissState) },
        content = {
            QuoteCard(
                quote = quote,
                onFavoriteClick = { toggleFavoriteQuote(currentItem) }
            )
        }
    )
}