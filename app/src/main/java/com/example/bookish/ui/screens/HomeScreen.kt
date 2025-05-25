package com.example.bookish.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookish.model.Book
import com.example.bookish.repositories.BookRepository
import com.example.bookish.ui.components.BookCard
import com.example.bookish.R
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, initialSearch: String = "") {
    val booksList = remember { mutableStateListOf<Book>() }
    var searchQuery by remember { mutableStateOf(initialSearch) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun loadSavedBooks() {
        isLoading = true
        booksList.clear()
        scope.launch {
            val saved = BookRepository.getAllBooksFromDatabase()
            booksList.addAll(saved)
            isLoading = false
        }
    }

    LaunchedEffect(initialSearch) {
        if (initialSearch.isNotBlank()) {
            isLoading = true
            val result = BookRepository.searchBooks(initialSearch)
            booksList.clear()
            booksList.addAll(result)
            isLoading = false
        } else {
            loadSavedBooks()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.isBlank()) loadSavedBooks()
                        },
                        label = { Text("Search by title") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        if (searchQuery.isNotBlank()) {
                            isLoading = true
                            booksList.clear()
                            scope.launch {
                                val result = BookRepository.searchBooks(searchQuery)
                                booksList.addAll(result)
                                isLoading = false
                            }
                        } else {
                            loadSavedBooks()
                        }
                    }) {
                        Text("Search")
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (booksList.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f)
                ) {
                    items(booksList) { book ->
                        BookCard(book = book) {
                            navController.navigate("details/${book.book.id}")
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.homescreenpicture),
                        contentDescription = "Home Screen Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}