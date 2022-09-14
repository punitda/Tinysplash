package dev.punitd.unplashapp.screen.photos

import app.cash.molecule.RecompositionClock
import dev.punitd.unplashapp.data.fake.FakeUnsplashRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class PhotosListViewModelTest {

    @Test
    fun initialState() = runTest {

        // Arrange
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(),
            scope = this,
            clock = RecompositionClock.Immediate,
        )


        val expectedUIState = PhotosListUIState(
            isLoading = false,
            error = null,
            images = listOf(),
        )

        assertEquals(expectedUIState, viewModel.stateFlow.value)
    }
}