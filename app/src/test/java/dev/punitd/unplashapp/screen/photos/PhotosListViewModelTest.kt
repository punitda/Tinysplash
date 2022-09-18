package dev.punitd.unplashapp.screen.photos

import app.cash.molecule.RecompositionClock
import app.cash.turbine.test
import dev.punitd.unplashapp.data.fake.FakeUnsplashRepository
import dev.punitd.unplashapp.model.*
import dev.punitd.unplashapp.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class PhotosListViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun initialState() = runTest {
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = backgroundScope, // TestScope
            clock = RecompositionClock.Immediate,
        )
        assertEquals(PhotosListUIState(isLoading = false), viewModel.stateFlow.value)
    }

    @Test
    fun successStateIfApiSucceeds() = runTest {
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = backgroundScope, // TestScope
            clock = RecompositionClock.Immediate,
        )

        viewModel.stateFlow.test {
            assertEquals(
                PhotosListUIState(isLoading = false),
                awaitItem()
            )
            assertEquals(
                PhotosListUIState(isLoading = true),
                awaitItem()
            )
            assertEquals(
                PhotosListUIState(
                    isLoading = false,
                    images = images,
                    pageLinks = pageLinks
                ), awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun errorStateIfApiFails() = runTest {
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(
                coroutineRule.testDispatcher,
                isSuccessful = false
            ),
            scope = backgroundScope, // TestScope
            clock = RecompositionClock.Immediate,
        )

        viewModel.stateFlow.test {
            assertEquals(PhotosListUIState(isLoading = false), awaitItem())
            assertEquals(PhotosListUIState(isLoading = true), awaitItem())
            assertEquals(
                PhotosListUIState(
                    isLoading = false,
                    error = FakeUnsplashRepository.PHOTOS_API_ERROR,
                ), awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun paginateSuccessIfApiSucceeds() = runTest {
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = backgroundScope, // TestScope
            clock = RecompositionClock.Immediate,
        )


        viewModel.stateFlow.test {
            assertEquals(PhotosListUIState(isLoading = false), awaitItem())
            assertEquals(PhotosListUIState(isLoading = true), awaitItem())
            assertEquals(
                PhotosListUIState(
                    isLoading = false,
                    images = images,
                    pageLinks = pageLinks
                ), awaitItem()
            )
            viewModel.processEvent(PaginateEvent)
            assertEquals(
                PhotosListUIState(
                    isPaginationLoading = true,
                    images = images,
                    pageLinks = pageLinks
                ),
                awaitItem()
            )
            assertEquals(
                PhotosListUIState(
                    isPaginationLoading = false,
                    images = images + otherImages,
                    pageLinks = otherPageLinks
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun paginateErrorIfApiFails() = runTest {
        val unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher)
        val viewModel = PhotosListViewModel(
            unsplashRepository = unsplashRepository,
            scope = backgroundScope, // TestScope
            clock = RecompositionClock.Immediate,
        )


        viewModel.stateFlow.test {
            assertEquals(PhotosListUIState(isLoading = false), awaitItem())
            assertEquals(PhotosListUIState(isLoading = true), awaitItem())
            assertEquals(
                PhotosListUIState(
                    isLoading = false,
                    images = images,
                    pageLinks = pageLinks
                ), awaitItem()
            )

            unsplashRepository.isSuccessful = false
            viewModel.processEvent(PaginateEvent)
            assertEquals(
                PhotosListUIState(
                    isPaginationLoading = true,
                    images = images,
                    pageLinks = pageLinks,
                ),
                awaitItem()
            )
            assertEquals(
                PhotosListUIState(
                    isPaginationLoading = false,
                    images = images,
                    pageLinks = pageLinks,
                    paginationError = FakeUnsplashRepository.PHOTOS_API_ERROR,
                ),
                awaitItem()
            )
            cancel()
        }
    }
}