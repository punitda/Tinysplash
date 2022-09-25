package dev.punitd.unplashapp.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.punitd.Semantics
import dev.punitd.Tags
import dev.punitd.unplashapp.MainActivity
import dev.punitd.unplashapp.model.firstPageHeaders
import dev.punitd.unplashapp.model.images
import dev.punitd.unplashapp.model.photosSuccessfulResponse
import dev.punitd.unplashapp.model.searchSuccessfulResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class TinySplashAppTest {
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
    fun gettingPhotosAndSearchingWorks() {
        assertLoaderIsShown()
        enqueueSuccessResponse(responseBody = photosSuccessfulResponse, firstPageHeaders)

        // Check images are displayed on photos tab
        assertImagesAreDisplayed(images.size, paginationLoader = true)
        // Check pagination loader is displayed on scroll
        assertPaginationLoaderIsDisplayed()

        // Sends success api response for next page with no link header(no next page)
        enqueueSuccessResponse(responseBody = photosSuccessfulResponse)
        // Wait till pagination loader is removed
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        // Images List should now show 2 pages worth of images
        assertImagesAreDisplayed(images.size * 2, paginationLoader = false)

        // Switch to search tab
        navigateToSearchTab()
        checkInitialSearchTabState()

        // Perform search & check images are displayed
        performSearchAndAssertFirstPageIsDisplayed("hi")

        // Scroll till bottom of list and check search pagination succeeds
        assertPaginationLoaderIsDisplayed()
        enqueueSuccessResponse(searchSuccessfulResponse, firstPageHeaders)

        // Check 2 pages of images are displayed to confirm pagination works as intended
        waitUntilInVisibleWithTag(Tags.PaginationLoader)
        assertImagesAreDisplayed(images.size * 2, paginationLoader = false)

        // Navigate back to photos tab, check loader shouldn't get displayed
        // and back stack state is maintained
        navigateToPhotosTab()
        composeTestRule.onNodeWithTag(Tags.ScreenLoader).assertDoesNotExist()

        // Navigate to search tab, search input shouldn't be empty
        // and images should be displayed
        navigateToSearchTab()
        composeTestRule.onNodeWithContentDescription(Semantics.SearchInput)
            .assertTextContains("hi")
        composeTestRule.onNodeWithTag(Tags.ImagesList).assertExists()
    }

    private fun assertPaginationLoaderIsDisplayed() {
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasTestTag(Tags.PaginationLoader))
        composeTestRule.onNodeWithTag(Tags.PaginationLoader).assertIsDisplayed()
    }

    private fun assertImagesAreDisplayed(size: Int, paginationLoader: Boolean = false) {
        val imagesListSize = if (paginationLoader) size + 1 else size
        val scrollIndex = if (paginationLoader) size else size - 1
        waitUntilVisibleWithTag(Tags.ImagesList)
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex(scrollIndex)
            .onChildren()
            .assertCountEquals(imagesListSize)
    }

    private fun checkInitialSearchTabState() {
        composeTestRule.onNodeWithContentDescription(Semantics.SearchInput).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.SearchSuggestionsLoader).assertDoesNotExist()
        composeTestRule.onNodeWithTag(Tags.ImagesList).onChildren().assertCountEquals(0)
    }

    private fun performSearchAndAssertFirstPageIsDisplayed(searchInput: String) {
        // Perform search & check first page list is displayed
        enqueueSuccessResponse(searchSuccessfulResponse, firstPageHeaders)
        search(searchInput)
        waitUntilInVisibleWithTag(Tags.SearchSuggestionsLoader)
        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex(images.lastIndex)
            .onChildren()
            .assertCountEquals(images.size + 1)
    }

    private fun search(searchInput: String) {
        composeTestRule.onNodeWithContentDescription(Semantics.SearchInput)
            .performTextInput(searchInput)
    }

    private fun waitUntilVisibleWithTag(tag: String) {
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(tag)
                .fetchSemanticsNodes().size == 1
        }
    }

    private fun waitUntilInVisibleWithTag(tag: String) {
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(tag)
                .fetchSemanticsNodes().isEmpty()
        }
    }

    private fun assertLoaderIsShown() {
        composeTestRule.onNodeWithTag(Tags.ScreenLoader).assertIsDisplayed()
    }

    private fun navigateToSearchTab() {
        composeTestRule.onNodeWithTag(Tags.SearchTab).performClick()
    }

    private fun navigateToPhotosTab() {
        composeTestRule.onNodeWithTag(Tags.PhotosTab).performClick()
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

}