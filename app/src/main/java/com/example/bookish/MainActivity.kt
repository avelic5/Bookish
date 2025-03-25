package com.example.bookish
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn


import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookish.ui.theme.BookishTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookishTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BookishAppLayout()
                }
            }
        }
    }
}

@Composable
fun BookishAppLayout() {
    var title by remember { mutableStateOf("") }

    var lista by remember { mutableStateOf(listOf<String>()) }
    Column(
        modifier = Modifier.statusBarsPadding()
            .padding(horizontal = 40.dp, vertical = 10.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditNameField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    lista = lista + title
                    title = ""
                }
            }
        )
        {
            Text("Add")
        }
        Text(
            text = "My favorite books are: ",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp).align(alignment = Alignment.Start),
            textAlign = TextAlign.Center,
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(lista) { item ->
                Text(
                    text = item,
                    fontSize = 20.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun EditNameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text("Book title")},
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun BookishPreview() {
    BookishTheme {
        BookishAppLayout()
    }
}