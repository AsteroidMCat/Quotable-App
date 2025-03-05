package com.zybooks.quotable.ui.theme

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class QuotesListViewModel : ViewModel() {

    private val quoteList = mutableStateListOf<Quote>()
    private val favoriteQuotes = mutableStateListOf<Quote>()

    fun getQuoteList(): SnapshotStateList<Quote> {
        return quoteList
    }

    fun addQuote(body: String, author: String) {
        if (body.isNotBlank()) {
            quoteList.add(Quote(body = body, author = author))
        }
    }

    fun deleteQuote(quote: Quote) {
        quoteList.remove(quote)
        favoriteQuotes.remove(quote)
    }

    fun getFavoriteQuotes(): List<Quote> {
        return favoriteQuotes
    }

    fun removeFavoriteQuote(quote: Quote) {
        favoriteQuotes.remove(quote)
    }

    val quotesExist: Boolean
        get() = quoteList.isNotEmpty()

    fun deleteFavoriteQuotes() {
        favoriteQuotes.clear()
    }

    fun deleteAllQuotes() {
        quoteList.clear()
    }

    fun toggleFavoriteQuote(quote: Quote) {
        // Comment from zyBooks: Observer of MutableList not notified when changing a property, so
        // need to replace element in the list for notification to go through
        val index = quoteList.indexOf(quote)

        if (quote.favorite) {
            quoteList[index] = quoteList[index].copy(favorite = false)
            removeFavoriteQuote(quote)
        } else {
            quoteList[index] = quoteList[index].copy(favorite = true)
            val newFavorite = Quote(quote.id, quote.body, quote.author, true)
            favoriteQuotes.add(newFavorite)
        }

    }
}