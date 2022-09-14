package dev.punitd.unplashapp.screen.photos

import app.cash.molecule.RecompositionClock
import app.cash.turbine.test
import dev.punitd.unplashapp.data.fake.FakeUnsplashRepository
import dev.punitd.unplashapp.model.images
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class PhotosListViewModelTest {

    @Test
    fun successStateIfApiSucceeds() = runTest {
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(),
            scope = this, // TestScope
            clock = RecompositionClock.Immediate,
        )

        // InitialPageEvent is send inside VM's init{} block by default
        // That's why we're not sending any events here.

        viewModel.stateFlow.test {
            assertEquals(PhotosListUIState(isLoading = false), awaitItem())
            assertEquals(PhotosListUIState(isLoading = true), awaitItem())
            runCurrent()
            assertEquals(
                PhotosListUIState(isLoading = false, error = null, images = images),
                awaitItem()
            )
        }
    }
}