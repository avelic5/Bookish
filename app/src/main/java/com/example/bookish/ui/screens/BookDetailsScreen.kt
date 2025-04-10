package com.example.bookish.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookish.model.Book
import com.example.bookish.R

@Composable
fun BookDetailsScreen(book: Book, onBack: () -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.books),
            contentDescription = "Book cover",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text("Authors: ${book.authors.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
        Text("Publisher: ${book.publisher}", style = MaterialTheme.typography.bodyMedium)
        Text("Categories: ${book.categories.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)

        Text(
            text = book.infoLink,
            color = Color.Blue,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = book.description,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}