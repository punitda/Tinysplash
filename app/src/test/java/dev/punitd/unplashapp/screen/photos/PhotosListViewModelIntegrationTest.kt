package dev.punitd.unplashapp.screen.photos

import app.cash.molecule.RecompositionClock
import dev.punitd.unplashapp.data.UnsplashApi
import dev.punitd.unplashapp.data.UnsplashRepositoryImpl
import dev.punitd.unplashapp.model.pageLinks
import dev.punitd.unplashapp.model.successNetworkResponse
import dev.punitd.unplashapp.util.Constants
import dev.punitd.unplashapp.util.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosListViewModelIntegrationTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val mockUnsplashApi: UnsplashApi = mockk()
    private val unsplashRepository = UnsplashRepositoryImpl(
        unsplashApi = mockUnsplashApi,
        ioDispatcher = coroutineRule.testDispatcher
    )

    @Test
    fun `init viewModel triggers getPhotos on apiClient`() = runTest {
        coEvery { mockUnsplashApi.getPhotos(any(), any()) } coAnswers { successNetworkResponse }

        PhotosListViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        runCurrent()
        coVerify(exactly = 1) {
            mockUnsplashApi.getPhotos(
                page = 1,
                perPage = Constants.ITEM_PER_PAGE
            )
        }
    }

    @Test
    fun `sending paginateEvent triggers getPhotosByUrl on apiClient`() = runTest {
        coEvery { mockUnsplashApi.getPhotos(any(), any()) } coAnswers { successNetworkResponse }
        coEvery { mockUnsplashApi.getPhotosByUrl(any()) } coAnswers { successNetworkResponse }

        val viewModel = PhotosListViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        runCurrent()
        viewModel.processEvent(PaginateEvent)
        runCurrent()
        coVerify { mockUnsplashApi.getPhotosByUrl(pageLinks.next!!) }
    }
}