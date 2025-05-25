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
    var book by remember { mutableStateOf<Book?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isRemoving by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    LaunchedEffect(id) {
        val fetchedBook = BookRepository.getBookById(id)
        book = fetchedBook
        if (fetchedBook != null) {
            BookRepository.saveToLocal(fetchedBook)
            isSaved = true
        }
        isLoading = false // <-- Add this line
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            book?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.book.thumbnail)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.books),
                    error = painterResource(R.drawable.books),
                    contentDescription = "Book cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = it.book.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            try {
                                val query = "${it.book.title} ${it.authors.joinToString(" ")}"
                                val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                                    putExtra(SearchManager.QUERY, query)
                                }
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                e.printStackTrace()
                            }
                        },
                    textAlign = TextAlign.Center
                )

                Text(
                    "Authors: ${it.authors.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Publisher: ${it.book.publisher}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Categories: ${it.categories.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = it.book.infoLink,
                    color = Color.Blue,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.book.infoLink))
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                )

                Text(
                    text = it.book.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable {
                        try {
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, it.book.description)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                val coroutineScope = rememberCoroutineScope()

                Button(
                    onClick = {
                        isRemoving = true
                        coroutineScope.launch {
                            BookRepository.removeBookFromDatabase(it.book.id)
                            isRemoving = false
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    enabled = !isRemoving
                ) {
                    Text("Remove from saved", color = Color.White)
                }
            } ?: Text("Greška: Knjiga nije pronađena.")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onBack() }) {
            Text("Back")
        }
    }
}
