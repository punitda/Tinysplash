package dev.punitd.unplashapp.screen.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import dev.punitd.Semantics
import dev.punitd.Tags
import org.junit.Rule
import org.junit.Test


class SearchWidgetKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun placeholderIsDisplayedWhenInputIsEmpty() {
        composeTestRule.setContent {
            SearchWidget(
                isSuggestionLoading = false,
                text = "",
                onTextChange = {},
                onClearClicked = {},
            )
        }

        composeTestRule.onNodeWithText("Search here..").assertIsDisplayed()
    }

    @Test
    fun inputTextIsDisplayedWhenEntered() {
        composeTestRule.setContent {
            SearchWidget(
                isSuggestionLoading = false,
                text = "hi",
                onTextChange = {},
                onClearClicked = {},
            )
        }

        composeTestRule.onNodeWithText("hi").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search here..").assertDoesNotExist()
    }

    @Test
    fun suggestionLoaderIsDisplayed() {
        composeTestRule.setContent {
            SearchWidget(
                isSuggestionLoading = true,
                text = "hi",
                onTextChange = {},
                onClearClicked = {},
            )
        }

        composeTestRule.onNodeWithText("hi").assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.SearchSuggestionsLoader).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(Semantics.ClearInput).assertDoesNotExist()

    }

    @Test
    fun clearTextIconButtonIsDisplayedWhenInputIsNotBlank() {
        composeTestRule.setContent {
            SearchWidget(
                isSuggestionLoading = false,
                text = "hi",
                onTextChange = {},
                onClearClicked = {},
            )
        }

        composeTestRule.onNodeWithText("hi").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(Semantics.ClearInput).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.SearchSuggestionsLoader).assertDoesNotExist()
    }
}