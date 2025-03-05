package com.zybooks.quotable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zybooks.quotable.ui.theme.AboutScreen
import com.zybooks.quotable.ui.theme.AddQuoteScreen
import com.zybooks.quotable.ui.theme.DeleteFavoriteQuotesDialog
import com.zybooks.quotable.ui.theme.FavoriteQuotesScreen
import com.zybooks.quotable.ui.theme.QuotableTheme
import com.zybooks.quotable.ui.theme.Quote
import com.zybooks.quotable.ui.theme.QuoteListScreen
import com.zybooks.quotable.ui.theme.QuotesListViewModel
import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Home

    @Serializable
    data object Favorites

    @Serializable
    data object AddQuote

    @Serializable
    data object About
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quotesListViewModel = QuotesListViewModel()
        enableEdgeToEdge()
        setContent {
            QuotableTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        var showDeleteAllQuotesDialog by remember { mutableStateOf(false) }
                        if (showDeleteAllQuotesDialog) {
                            DeleteFavoriteQuotesDialog(
                                onDismiss = { showDeleteAllQuotesDialog = false },
                                onConfirm = {
                                    quotesListViewModel.deleteAllQuotes()
                                    quotesListViewModel.deleteFavoriteQuotes()
                                    showDeleteAllQuotesDialog = false
                                }
                            )
                        }
                        TopAppBar(
                            title = { Text("Quotable!") },
                            actions = {
                                IconButton(onClick = {
                                    if (quotesListViewModel.quotesExist) {
                                        showDeleteAllQuotesDialog = true
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete all quotes"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(navController)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Routes.Home,
                        modifier = Modifier.padding(innerPadding),

                        ) {
                        composable<Routes.Home> {

                            Home(
                                quotesListViewModel::getQuoteList,
                                quotesListViewModel::deleteQuote,
                                quotesListViewModel::toggleFavoriteQuote,
                                modifier = Modifier.padding()
                            )
                        }
                        composable<Routes.AddQuote> {
                            AddQuote(quotesListViewModel::addQuote)
                        }
                        composable<Routes.Favorites> {
                            Favorites(
                                quotesListViewModel::getFavoriteQuotes,
                                quotesListViewModel::removeFavoriteQuote,
                                quotesListViewModel::toggleFavoriteQuote,
                                modifier = Modifier.padding()
                            )
                        }
                        composable<Routes.About> {
                            About()
                        }
                    }
                }
            }
        }
    }

    enum class AppScreen(val route: Any, val title: String, val icon: ImageVector) {
        HOME(Routes.Home, "Home", Icons.Filled.Home),
        FAVORITES(Routes.Favorites, "Favorites", Icons.Filled.Favorite),
        ADDQUOTE(Routes.AddQuote, "Add", Icons.Filled.Add),
        DEVELOPER(Routes.About, "About", Icons.Filled.Info),
    }

    @Composable
    fun BottomNavBar(
        navController: NavController
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavigationBar {
            AppScreen.entries.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute?.endsWith(item.route.toString()) == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    icon = {
                        Icon(item.icon, contentDescription = item.title)
                    },
                    label = {
                        Text(item.title)
                    }

                )
            }
        }
    }

    @Composable
    fun Home(
        getQuotesList: () -> List<Quote>,
        deleteQuote: (Quote) -> Unit,
        toggleFavoriteQuote: (Quote) -> Unit,
        modifier: Modifier
    ) {
        QuoteListScreen(
            getQuotesList,
            deleteQuote,
            toggleFavoriteQuote,
            modifier
        )
    }

    @Composable
    fun AddQuote(addQuote: (String, String) -> Unit) {
        AddQuoteScreen(addQuote)
    }

    @Composable
    fun Favorites(
        getFavoriteQuotes: () -> List<Quote>,
        removeFavoriteQuote: (Quote) -> Unit,
        toggleFavoriteQuote: (Quote) -> Unit,
        modifier: Modifier
    ) {
        FavoriteQuotesScreen(
            getFavoriteQuotes,
            removeFavoriteQuote,
            toggleFavoriteQuote,
            modifier
        )
    }

    @Composable
    fun About() {
        AboutScreen()
    }
}