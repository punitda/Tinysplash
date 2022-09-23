package dev.punitd.unplashapp.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import coil.annotation.ExperimentalCoilApi
import dev.punitd.Semantics
import dev.punitd.Tags
import dev.punitd.unplashapp.model.images
import dev.punitd.unplashapp.model.pageLinks
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoilApi::class)
class PhotosListKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun photosListIsDisplayedWhenImagesAreProvided() {
        composeTestRule.setContent {
            PhotosList(
                items = images,
                pageLinks = null,
                paginationError = null,
                isPaginationLoading = false,
                onRetryPaginationClicked = { },
                loadNextPage = { },
            )
        }

        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .onChildren()
            .assertCountEquals(images.size)
    }

    @Test
    fun paginationLoaderIsDisplayedWhenLoadingNextPage() {
        composeTestRule.setContent {
            PhotosList(
                items = images,
                pageLinks = pageLinks,
                isPaginationLoading = true,
                paginationError = null,
                onRetryPaginationClicked = { },
                loadNextPage = { },
            )
        }

        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasTestTag(Tags.PaginationLoader))

        composeTestRule.onNodeWithTag(Tags.PaginationLoader).assertIsDisplayed()
    }

    @Test
    fun paginationErrorIsDisplayedWhenPaginationErrorOccurs() {
        val error = "Something went wrong"
        composeTestRule.setContent {
            PhotosList(
                items = images,
                pageLinks = pageLinks,
                isPaginationLoading = false,
                paginationError = error,
                onRetryPaginationClicked = {},
                loadNextPage = { },
            )
        }

        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasText(error))

        composeTestRule.onNodeWithText(error).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(Semantics.RetryAction).assertIsDisplayed()
    }

    @Test
    fun loadNextPageIsCalledOnScroll() {
        val loadNextPage: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            PhotosList(
                items = images,
                pageLinks = pageLinks,
                isPaginationLoading = false,
                paginationError = null,
                onRetryPaginationClicked = { },
                loadNextPage = loadNextPage,
            )
        }

        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToIndex(images.lastIndex)

        verify(exactly = 1) { loadNextPage() }
    }

    @Test
    fun onRetryPaginationClickedIsCalled() {
        val error = "Something went wrong"
        val onRetryInitialPageClicked: () -> Unit = mockk(relaxed = true)
        composeTestRule.setContent {
            PhotosList(
                items = images,
                pageLinks = pageLinks,
                isPaginationLoading = false,
                paginationError = error,
                onRetryPaginationClicked = onRetryInitialPageClicked,
                loadNextPage = { },
            )
        }

        composeTestRule.onNodeWithTag(Tags.ImagesList)
            .performScrollToNode(hasText(error))

        composeTestRule.onNodeWithContentDescription(Semantics.RetryAction).performClick()
        verify(exactly = 1) { onRetryInitialPageClicked() }
    }
}