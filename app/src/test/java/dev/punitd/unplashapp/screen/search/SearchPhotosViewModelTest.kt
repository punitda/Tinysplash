package dev.punitd.unplashapp.screen.search

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import dev.punitd.unplashapp.data.fake.FakeUnsplashRepository
import dev.punitd.unplashapp.model.images
import dev.punitd.unplashapp.model.otherImages
import dev.punitd.unplashapp.model.otherPageLinks
import dev.punitd.unplashapp.model.pageLinks
import dev.punitd.unplashapp.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SearchPhotosViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun initialState() = runTest {
        val viewModel = SearchPhotosViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )
        assertEquals(SearchUIState(isLoading = false), viewModel.stateFlow.value)
    }


    @Test
    fun searchSuccessStateIfApiSucceeds() = runTest {
        val viewModel = SearchPhotosViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        val events = MutableSharedFlow<Event>()
        moleculeFlow(RecompositionClock.Immediate) {
            viewModel.present(events)
        }.test {
            assertEquals(
                SearchUIState(isLoading = false),
                awaitItem()
            )

            events.emit(SearchEvent("m"))
            assertEquals(
                SearchUIState(isLoading = true, currentQuery = "m"),
                awaitItem()
            )
            events.emit(SearchEvent("mai"))
            assertEquals(
                SearchUIState(isLoading = true, currentQuery = "mai"),
                awaitItem()
            )

            events.emit(SearchEvent("main"))
            assertEquals(
                SearchUIState(isLoading = true, currentQuery = "main"),
                awaitItem()
            )
            assertEquals(
                SearchUIState(
                    isLoading = false, images = images, pageLinks = pageLinks,
                    currentQuery = "main"
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun searchErrorStateIfApiFails() = runTest {
        val viewModel = SearchPhotosViewModel(
            unsplashRepository = FakeUnsplashRepository(
                coroutineRule.testDispatcher,
                isSuccessful = false
            ),
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        val events = MutableSharedFlow<Event>()
        moleculeFlow(RecompositionClock.Immediate) {
            viewModel.present(events)
        }.test {
            assertEquals(
                SearchUIState(isLoading = false),
                awaitItem()
            )

            events.emit(SearchEvent("m"))
            assertEquals(
                SearchUIState(isLoading = true, currentQuery = "m"),
                awaitItem()
            )
            assertEquals(
                SearchUIState(
                    isLoading = false,
                    error = FakeUnsplashRepository.SEARCH_API_ERROR,
                    currentQuery = "m"
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun searchPaginateSuccessStateIfPaginationApiSucceeds() = runTest {
        val viewModel = SearchPhotosViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        val events = MutableSharedFlow<Event>()
        moleculeFlow(RecompositionClock.Immediate) {
            viewModel.present(events)
        }.test {
            assertEquals(
                SearchUIState(isLoading = false),
                awaitItem()
            )

            events.emit(SearchEvent("main"))
            assertEquals(
                SearchUIState(isLoading = true, currentQuery = "main"),
                awaitItem()
            )
            assertEquals(
                SearchUIState(
                    isLoading = false,
                    images = images,
                    pageLinks = pageLinks,
                    currentQuery = "main"
                ),
                awaitItem()
            )

            // Paginate
            events.emit(PaginateEvent)
            assertEquals(
                SearchUIState(
                    isPaginationLoading = true,
                    images = images,
                    pageLinks = pageLinks,
                    currentQuery = "main"
                ),
                awaitItem()
            )
            assertEquals(
                SearchUIState(
                    isPaginationLoading = false,
                    images = images + otherImages,
                    pageLinks = otherPageLinks,
                    currentQuery = "main"
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun searchPaginateErrorStateIfPaginationApiFails() = runTest {
        val unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher)
        val viewModel = SearchPhotosViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope,
            clock = RecompositionClock.Immediate,
        )

        val events = MutableSharedFlow<Event>()
        moleculeFlow(RecompositionClock.Immediate) {
            viewModel.present(events)
        }.test {
            assertEquals(
                SearchUIState(isLoading = false),
                awaitItem()
            )

            events.emit(SearchEvent("main"))
            assertEquals(
                SearchUIState(isLoading = true, currentQuery = "main"),
                awaitItem()
            )
            assertEquals(
                SearchUIState(
                    isLoading = false,
                    images = images,
                    pageLinks = pageLinks,
                    currentQuery = "main"
                ),
                awaitItem()
            )

            // Paginate
            unsplashRepository.isSuccessful = false
            events.emit(PaginateEvent)

            assertEquals(
                SearchUIState(
                    isPaginationLoading = true,
                    images = images,
                    pageLinks = pageLinks,
                    currentQuery = "main"
                ),
                awaitItem()
            )
            assertEquals(
                SearchUIState(
                    isPaginationLoading = false,
                    images = images,
                    pageLinks = pageLinks,
                    paginationError = FakeUnsplashRepository.SEARCH_API_ERROR,
                    currentQuery = "main"
                ),
                awaitItem()
            )
            cancel()
        }
    }
}

