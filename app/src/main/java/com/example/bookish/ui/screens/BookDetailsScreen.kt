package com.example.bookish.ui.screens

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookish.R
import com.example.bookish.model.Book
import com.example.bookish.repositories.BookRepository
import kotlinx.coroutines.launch


@Composable
fun BookDetailsScreen(id: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { BookRepository(context) }
    var book by remember { mutableStateOf<Book?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        isLoading = true
        book = repository.getBookByIdFromProvider(id,context)
        isLoading = false
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                book?.let {
                    AsyncImage(
                        model = it.book.thumbnail,
                        contentDescription = "Book cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.book.title, style = MaterialTheme.typography.headlineSmall)
                    Text("Authors: ${it.authors.joinToString { a -> a.name }}")
                    Text("Publisher: ${it.book.publisher}")
                    Text("Categories: ${it.categories.joinToString { c -> c.name }}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.book.description)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        scope.launch {
                            repository.deleteFromProvider(it,context)
                            snackbarHostState.showSnackbar("Book was removed from database.")
                            onBack()
                        }
                    }) {
                        Text("Remove from database")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onBack) {
                        Text("Back")
                    }
                } ?: Text("Book was not found")
            }
        }
    }
}

