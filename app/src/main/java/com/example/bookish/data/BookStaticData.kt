package com.example.bookish.data

import com.example.bookish.model.Book
import com.example.bookish.model.BookEntity
import com.example.bookish.model.Author
import com.example.bookish.model.Category

object BookStaticData {

    fun getSampleBooks(): List<Book> {
        return listOf(
            Book(
                book = BookEntity(
                    id = "1",
                    title = "The Great Gatsby",
                    publisher = "Scribner",
                    infoLink = "https://books.google.ba/books/about/The_Great_Gatsby.html?id=iXn5U2IzVH0C&redir_esc=y",
                    description = "A novel set in the Jazz Age that tells the story of the mysterious millionaire Jay Gatsby and his obsession with Daisy Buchanan.",
                    thumbnail = ""
                ),
                authors = listOf(Author(name = "F. Scott Fitzgerald")),
                categories = listOf(
                    Category(name = "Fiction"),
                    Category(name = "Classic")
                )
            ),
            Book(
                book = BookEntity(
                    id = "2",
                    title = "1984",
                    publisher = "Secker & Warburg",
                    infoLink = "https://books.google.ba/books/about/1984.html?id=kotPYEqx7kMC&redir_esc=y",
                    description = "A dystopian novel about a totalitarian regime that employs surveillance and propaganda to oppress its citizens.",
                    thumbnail = ""
                ),
                authors = listOf(Author(name = "George Orwell")),
                categories = listOf(
                    Category(name = "Dystopian"),
                    Category(name = "Science Fiction")
                )
            ),
            Book(
                book = BookEntity(
                    id = "3",
                    title = "Clean Code",
                    publisher = "Prentice Hall",
                    infoLink = "https://www.google.ba/books/edition/Clean_Code/_i6bDeoCQzsC?hl=en",
                    description = "A handbook of agile software craftsmanship, teaching the principles of writing clean and maintainable code.",
                    thumbnail = ""
                ),
                authors = listOf(Author(name = "Robert C. Martin")),
                categories = listOf(
                    Category(name = "Programming"),
                    Category(name = "Software Engineering")
                )
            ),
            Book(
                book = BookEntity(
                    id = "4",
                    title = "Pride and Prejudice",
                    publisher = "T. Egerton, Whitehall",
                    infoLink = "https://books.google.ba/books/about/Pride_and_Prejudice.html?id=s1gVAAAAYAAJ&redir_esc=y",
                    description = "A romantic novel that also critiques the British landed gentry at the end of the 18th century.",
                    thumbnail = ""
                ),
                authors = listOf(Author(name = "Jane Austen")),
                categories = listOf(
                    Category(name = "Romance"),
                    Category(name = "Classic")
                )
            ),
            Book(
                book = BookEntity(
                    id = "5",
                    title = "The Pragmatic Programmer",
                    publisher = "Addison-Wesley",
                    infoLink = "https://books.google.ba/books/about/The_Pragmatic_Programmer.html?id=5wBQEp6ruIAC&redir_esc=y",
                    description = "A guide for software developers that offers practical advice on software construction and career development.",
                    thumbnail = ""
                ),
                authors = listOf(
                    Author(name = "Andrew Hunt"),
                    Author(name = "David Thomas")
                ),
                categories = listOf(
                    Category(name = "Programming"),
                    Category(name = "Technology")
                )
            ),
            Book(
                book = BookEntity(
                    id = "6",
                    title = "To Kill a Mockingbird",
                    publisher = "J.B. Lippincott & Co.",
                    infoLink = "https://books.google.ba/books/about/To_Kill_a_Mockingbird.html?id=0NEbHGREK7cC&redir_esc=y",
                    description = "A novel about the serious issues of rape and racial inequality, told through the eyes of a child in the American South.",
                    thumbnail = ""
                ),
                authors = listOf(Author(name = "Harper Lee")),
                categories = listOf(
                    Category(name = "Fiction"),
                    Category(name = "Social Justice")
                )
            )
        )
    }
}