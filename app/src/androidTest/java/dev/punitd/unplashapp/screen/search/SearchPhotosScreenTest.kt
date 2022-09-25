package dev.punitd.unplashapp.screen.search

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.punitd.Semantics
import dev.punitd.Tags
import dev.punitd.unplashapp.MainActivity
import dev.punitd.unplashapp.model.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchPhotosScreenTest {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        hiltRule.inject()
        navigateToSearchTab()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun searchSuccess() {
        checkInitialSearchTabState()
        performSearchAndAssertFirstPageIsDisplayed()
    }

    @Test
    fun searchError() {
        checkInitialSearchTabState()

        // Perform search & check snackbar error is displayed
        enqueueErrorResponse(errorResponse)
        search("h")
        waitUntilInVisibleWithTag(Tags.SearchSuggestionsLoader)
        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun searchPaginationSuccess() {
        checkInitialSearchTabState()

        // Perform search & check images are displayed
        performSearchAndAssertFirstPageIsDisplayed()

        // Scroll till bottom of list and check pagination succeeds
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasTestTag(Tags.PaginationLoader))
        composeTestRule.onNodeWithTag(Tags.PaginationLoader).assertIsDisplayed()
        enqueueSuccessResponse(searchSuccessfulResponse)

        // Check 2 pages of images are displayed to confirm pagination works as intended
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex((images + images).lastIndex)
            .onChildren()
            .assertCountEquals(images.size * 2)
    }

    @Test
    fun searchPaginationError() {
        checkInitialSearchTabState()

        // Perform search & check images are displayed
        performSearchAndAssertFirstPageIsDisplayed()

        // Scroll to bottom of list to paginate
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasTestTag(Tags.PaginationLoader))
        composeTestRule.onNodeWithTag(Tags.PaginationLoader).assertIsDisplayed()
        // Sends error api response for next page to show pagination error
        enqueueErrorResponse(responseBody = errorResponse)

        // Wait till pagination loader is removed and verify pagination error ui is displayed
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(Semantics.RetryAction).assertIsDisplayed()

        // Check retry pagination action works
        enqueueSuccessResponse(searchSuccessfulResponse)
        composeTestRule.onNodeWithContentDescription(Semantics.RetryAction).performClick()
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex((images + images).lastIndex)
            .onChildren()
            .assertCountEquals(images.size * 2)
    }

    private fun navigateToSearchTab() {
        // Need to emit success for photos screen first
        // so that MockWebServer enqueuing works correctly
        // Navigate to search tab
        composeTestRule.onNodeWithTag(Tags.SearchTab).performClick()
        enqueueSuccessResponse(photosSuccessfulResponse, firstPageHeaders)
    }

    private fun checkInitialSearchTabState() {
        composeTestRule.onNodeWithContentDescription(Semantics.SearchInput).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.SearchSuggestionsLoader).assertDoesNotExist()
        composeTestRule.onNodeWithTag(Tags.ImagesList).onChildren().assertCountEquals(0)
    }

    private fun search(searchInput: String) {
        composeTestRule.onNodeWithContentDescription(Semantics.SearchInput)
            .performTextInput(searchInput)
    }

    private fun performSearchAndAssertFirstPageIsDisplayed() {
        // Perform search & check first page list is displayed
        enqueueSuccessResponse(searchSuccessfulResponse, firstPageHeaders)
        search("hi")
        waitUntilInVisibleWithTag(Tags.SearchSuggestionsLoader)
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex(images.lastIndex)
            .onChildren()
            .assertCountEquals(images.size + 1)
    }


    private fun waitUntilInVisibleWithTag(tag: String) {
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(tag)
                .fetchSemanticsNodes().isEmpty()
        }
    }

    private fun enqueueSuccessResponse(responseBody: String, headers: okhttp3.Headers? = null) {
        mockWebServer.enqueue(
            MockResponse()
                .apply {
                    setBody(responseBody)
                    if (headers != null) setHeader(headers.name(0), headers.value(0))
                    setResponseCode(200)
                }
        )
    }

    private fun enqueueErrorResponse(responseBody: String, headers: okhttp3.Headers? = null) {
        mockWebServer.enqueue(
            MockResponse()
                .apply {
                    setBody(responseBody)
                    if (headers != null) setHeader(headers.name(0), headers.value(0))
                    setResponseCode(400)
                }
        )
    }

}