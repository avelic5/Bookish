package com.example.bookish.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookish.model.Book

import com.example.bookish.repositories.BookRepository
import com.example.bookish.ui.components.BookCard
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, initialSearch: String = "") {
    val booksList = remember { mutableStateListOf<Book>() } // lista knjiga koju UI posmatra
    var searchQuery by remember { mutableStateOf(initialSearch) } // trenutno uneseni tekst
    var isLoading by remember { mutableStateOf(false) } // indikator učitavanja
    val scope = rememberCoroutineScope() // korutinski scope za pokretanje mrežnih poziva

    // Ako postoji početni tekst (npr. iz intent-a), odmah pokreni pretragu
    LaunchedEffect(initialSearch) {
        if (initialSearch.isNotBlank()) {
            isLoading = true
            val result = BookRepository.searchBooks(initialSearch)
            booksList.clear()
            booksList.addAll(result)
            isLoading = false
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // UI dio za unos pretrage
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
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
                }
            }) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prikaz loading indikatora
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Prikaz rezultata
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(booksList) { book ->
                BookCard(book = book) {
                    navController.navigate("details/${book.id}")
                }
            }
        }
    }
}