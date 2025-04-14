package com.example.bookish

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookish.data.BookStaticData
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RunWith(AndroidJUnit4::class)
class BookishInstrumentedTests {


    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun allBooksAreDisplayedOnHomeScreen() {
        val books = BookStaticData.getSampleBooks()

        books.forEach { book ->
            composeTestRule.onNodeWithText(book.title, substring = true)
                .assertExists("Book '${book.title}' is not displayed")// ako padne ovo ispisi
        }
    }


    @Test
    fun clickingBookNavigatesToDetailsAndDisplaysCorrectInfo() {
        val book = BookStaticData.getSampleBooks().first()

        composeTestRule.onNodeWithText(book.title, substring = true)
            .performClick()

        composeTestRule.onNodeWithText(book.title, substring = true).assertExists()
        composeTestRule.onNodeWithText("Authors: ${book.authors.joinToString(", ")}").assertExists()
        composeTestRule.onNodeWithText("Publisher: ${book.publisher}").assertExists()
        composeTestRule.onNodeWithText("Categories: ${book.categories.joinToString(", ")}").assertExists()
        composeTestRule.onNodeWithText(book.description, substring = true).assertExists()
    }
    private fun hasSendIntentInsideChooser(bookDescription: String): Matcher<Intent> {
        return allOf(
            IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
            hasExtra(`is`(Intent.EXTRA_INTENT), allOf(
                IntentMatchers.hasAction(Intent.ACTION_SEND),
                hasExtra(Intent.EXTRA_TEXT, bookDescription),
                IntentMatchers.hasType("text/plain")
            ))
        )
    }
    @Test
    fun shareIntentIsTriggeredOnDescriptionClick() {
        val book = BookStaticData.getSampleBooks().first()

        init()

        composeTestRule.onNodeWithText(book.title, substring = true).performClick()
        composeTestRule.onNodeWithText(book.description, substring = true).performClick()

        intended(hasSendIntentInsideChooser(book.description))

        release()
    }
    @Test//ne radi
    fun clickingBookLinkOpensWebPage() {
        val book = BookStaticData.getSampleBooks().first()
        val expectedUri = book.infoLink

        init()

        composeTestRule.onNodeWithText(book.title, substring = true).performClick()

        // Debug: Print the semantics tree to verify the node
        composeTestRule.onRoot().printToLog("TAG")

        // Use a flexible matcher and increase timeout
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule.onAllNodesWithText("Otvorite više informacija", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Otvorite više informacija", substring = true).performClick()

        intended(allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasData(Uri.parse(expectedUri))
        ))

        release()
    }





}