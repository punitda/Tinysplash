package dev.punitd.unplashapp.screen.photos

import app.cash.molecule.RecompositionClock
import app.cash.turbine.testIn
import dev.punitd.unplashapp.data.fake.FakeUnsplashRepository
import dev.punitd.unplashapp.model.images
import dev.punitd.unplashapp.model.pageLinks
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
    fun successStateIfApiSucceeds() = runTest {
        val viewModel = PhotosListViewModel(
            unsplashRepository = FakeUnsplashRepository(coroutineRule.testDispatcher),
            scope = this, // TestScope
            clock = RecompositionClock.Immediate,
        )

        // InitialPageEvent is send inside VM's init{} block by default
        // That's why we're not sending any events here.

        val turbine = viewModel.stateFlow.testIn(this)
        assertEquals(PhotosListUIState(isLoading = false), turbine.awaitItem())
        assertEquals(PhotosListUIState(isLoading = true), turbine.awaitItem())
        assertEquals(
            PhotosListUIState(
                isLoading = false,
                error = null,
                images = images,
                pageLinks = pageLinks
            ),
            turbine.awaitItem()
        )
        turbine.cancel()
    }
}