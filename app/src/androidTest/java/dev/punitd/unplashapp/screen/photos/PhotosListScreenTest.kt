package dev.punitd.unplashapp.screen.photos

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.punitd.Semantics
import dev.punitd.Tags
import dev.punitd.unplashapp.MainActivity
import dev.punitd.unplashapp.model.*
import kotlinx.coroutines.delay
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PhotosListScreenTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun photosListFetchSuccess() {
        assertLoaderIsShown()
        // Sends success api response with no link page header(no next pages)
        enqueueSuccessResponse(responseBody = photosSuccessfulResponse)

        assertFirstPageImagesIsDisplayed(images.size)
        // Checks pagination loader is not shown when next page doesn't exist
        composeTestRule.onNodeWithTag(Tags.PaginationLoader).assertDoesNotExist()
    }

    @Test
    fun photosListFetchError() {
        assertLoaderIsShown()

        // Sends success api response with no link page header(no next pages)
        enqueueErrorResponse(responseBody = errorResponse)

        // Wait for data to be fetched and show error
        waitUntilVisibleWithText(errorMessage)
        composeTestRule.onNodeWithContentDescription(Semantics.RetryAction).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.ImagesList).assertDoesNotExist()
    }

    @Test
    fun photosListPaginationSuccess() {
        assertLoaderIsShown()

        // Sends success api response with link page header
        enqueueSuccessResponse(responseBody = photosSuccessfulResponse, headers = firstPageHeaders)
        assertFirstPageImagesIsDisplayed(images.size, paginationLoader = true)
        assertPaginationLoaderIsDisplayed()

        // Sends success api response for next page with no link header(no next page)
        enqueueSuccessResponse(responseBody = photosSuccessfulResponse)
        // Wait till pagination loader is removed
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        // Images List should now show 2 pages worth of images
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex((images + images).lastIndex)
            .onChildren()
            .assertCountEquals(images.size * 2)
    }

    @Test
    fun photosListPaginationError() {
        assertLoaderIsShown()

        // Sends success api response with link page header
        enqueueSuccessResponse(responseBody = photosSuccessfulResponse, headers = firstPageHeaders)
        assertFirstPageImagesIsDisplayed(images.size, paginationLoader = true)
        assertPaginationLoaderIsDisplayed()

        // Sends error api response for next page to show pagination error
        enqueueErrorResponse(responseBody = errorResponse)
        // Wait till pagination loader is removed and verify pagination error ui is displayed
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(Semantics.RetryAction).assertIsDisplayed()
    }

    private fun assertLoaderIsShown() {
        composeTestRule.onNodeWithTag(Tags.ScreenLoader).assertIsDisplayed()
    }

    private fun assertFirstPageImagesIsDisplayed(size: Int, paginationLoader: Boolean = false) {
        val imagesListSize = if (paginationLoader) size + 1 else size
        waitUntilVisibleWithTag(Tags.ImagesList)
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex(images.lastIndex)
            .onChildren()
            .assertCountEquals(imagesListSize)
    }

    private fun assertPaginationLoaderIsDisplayed() {
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasTestTag(Tags.PaginationLoader))
        composeTestRule.onNodeWithTag(Tags.PaginationLoader).assertIsDisplayed()
    }


    private fun waitUntilVisibleWithTag(tag: String) {
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(tag)
                .fetchSemanticsNodes().size == 1
        }
    }

    private fun waitUntilVisibleWithText(text: String) {
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(text)
                .fetchSemanticsNodes().size == 1
        }
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
                    setResponseCode(500)
                }
        )
    }

}