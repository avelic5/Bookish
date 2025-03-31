package com.example.bookish

object BookStaticData {

    fun getSampleBooks(): List<Book> {
        return listOf(
            Book(
                title = "The Great Gatsby",
                authors = listOf("F. Scott Fitzgerald"),
                publisher = "Scribner",
                categories = listOf("Fiction", "Classic"),
                infoLink = "https://books.google.ba/books/about/The_Great_Gatsby.html?id=iXn5U2IzVH0C&redir_esc=y",
                description = "A novel set in the Jazz Age that tells the story of the mysterious millionaire Jay Gatsby and his obsession with Daisy Buchanan."
            ),
            Book(
                title = "1984",
                authors = listOf("George Orwell"),
                publisher = "Secker & Warburg",
                categories = listOf("Dystopian", "Science Fiction"),
                infoLink = "https://books.google.ba/books/about/1984.html?id=kotPYEqx7kMC&redir_esc=y",
                description = "A dystopian novel about a totalitarian regime that employs surveillance and propaganda to oppress its citizens."
            ),
            Book(
                title = "Clean Code",
                authors = listOf("Robert C. Martin"),
                publisher = "Prentice Hall",
                categories = listOf("Programming", "Software Engineering"),
                infoLink = "https://www.google.ba/books/edition/Clean_Code/_i6bDeoCQzsC?hl=en",
                description = "A handbook of agile software craftsmanship, teaching the principles of writing clean and maintainable code."
            ),
            Book(
                title = "Pride and Prejudice",
                authors = listOf("Jane Austen"),
                publisher = "T. Egerton, Whitehall",
                categories = listOf("Romance", "Classic"),
                infoLink = "https://books.google.ba/books/about/Pride_and_Prejudice.html?id=s1gVAAAAYAAJ&redir_esc=y",
                description = "A romantic novel that also critiques the British landed gentry at the end of the 18th century."
            ),
            Book(
                title = "The Pragmatic Programmer",
                authors = listOf("Andrew Hunt", "David Thomas"),
                publisher = "Addison-Wesley",
                categories = listOf("Programming", "Technology"),
                infoLink = "https://books.google.ba/books/about/The_Pragmatic_Programmer.html?id=5wBQEp6ruIAC&redir_esc=y",
                description = "A guide for software developers that offers practical advice on software construction and career development."
            ),
            Book(
                title = "To Kill a Mockingbird",
                authors = listOf("Harper Lee"),
                publisher = "J.B. Lippincott & Co.",
                categories = listOf("Fiction", "Social Justice"),
                infoLink = "https://books.google.ba/books/about/To_Kill_a_Mockingbird.html?id=0NEbHGREK7cC&redir_esc=y",
                description = "A novel about the serious issues of rape and racial inequality, told through the eyes of a child in the American South."
            )
        )
    }
}