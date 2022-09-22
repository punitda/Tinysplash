package dev.punitd.unplashapp.screen.search

import app.cash.molecule.RecompositionClock
import dev.punitd.unplashapp.data.UnsplashApi
import dev.punitd.unplashapp.data.UnsplashRepositoryImpl
import dev.punitd.unplashapp.model.pageLinks
import dev.punitd.unplashapp.model.successSearchNetworkResponse
import dev.punitd.unplashapp.util.Constants
import dev.punitd.unplashapp.util.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPhotosViewModelIntegrationTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val mockUnsplashApi: UnsplashApi = mockk()
    private val unsplashRepository = UnsplashRepositoryImpl(
        unsplashApi = mockUnsplashApi,
        ioDispatcher = coroutineRule.testDispatcher
    )

    @Test
    fun `init viewModel doesn't triggers any method on apiClient`() = runTest {
        coEvery { mockUnsplashApi.search(any(), any()) } coAnswers { successSearchNetworkResponse }

        SearchPhotosViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        runCurrent()
        coVerify(exactly = 0) {
            mockUnsplashApi.search(query = "", perPage = Constants.ITEM_PER_PAGE)
        }
    }

    @Test
    fun `sending searchEvent triggers search on apiClient`() = runTest {
        coEvery { mockUnsplashApi.search(any(), any()) } coAnswers { successSearchNetworkResponse }

        val viewModel = SearchPhotosViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        runCurrent()
        viewModel.processEvent(SearchEvent("q"))
        advanceTimeBy(SearchPhotosViewModel.SEARCH_TIMEOUT)
        runCurrent()
        coVerify(exactly = 1) {
            mockUnsplashApi.search(
                query = "q",
                perPage = Constants.ITEM_PER_PAGE
            )
        }
    }

    @Test
    fun `sending immediate searchEvent before debounceTime only triggers single search on apiClient`() =
        runTest {
            coEvery {
                mockUnsplashApi.search(
                    any(),
                    any()
                )
            } coAnswers { successSearchNetworkResponse }

            val viewModel = SearchPhotosViewModel(
                unsplashRepository = unsplashRepository,
                scope = backgroundScope,
                clock = RecompositionClock.Immediate,
            )

            runCurrent()
            viewModel.processEvent(SearchEvent("q"))
            viewModel.processEvent(SearchEvent("qu"))
            viewModel.processEvent(SearchEvent("que"))
            advanceTimeBy(SearchPhotosViewModel.SEARCH_TIMEOUT)
            runCurrent()
            coVerify(exactly = 1) {
                mockUnsplashApi.search(
                    query = "que",
                    perPage = Constants.ITEM_PER_PAGE
                )
            }
        }

    @Test
    fun `sending paginateEvent triggers before searchPhotosByUrl on apiClient`() = runTest {
        coEvery { mockUnsplashApi.search(any(), any()) } coAnswers { successSearchNetworkResponse }
        coEvery { mockUnsplashApi.searchPhotosByUrl(any()) } coAnswers { successSearchNetworkResponse }

        val viewModel = SearchPhotosViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        runCurrent()
        viewModel.processEvent(SearchEvent("q"))
        advanceTimeBy(SearchPhotosViewModel.SEARCH_TIMEOUT)
        runCurrent()
        viewModel.processEvent(PaginateEvent)
        runCurrent()
        coVerify(exactly = 1) { mockUnsplashApi.searchPhotosByUrl(pageLinks.next!!) }
    }
}