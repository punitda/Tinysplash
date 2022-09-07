@file:OptIn(ExperimentalCoilApi::class)

package dev.punitd.unplashapp.screen.photos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import dev.punitd.unplashapp.components.PhotosList


@Composable
fun PhotosListScreenRoute(modifier: Modifier) {
    val viewModel: PhotosListViewModel = hiltViewModel()
    val state by viewModel.stateFlow.collectAsState()
    PhotosListScreen(
        modifier = modifier,
        state = state,
        onRetryInitialPageClicked = { viewModel.processEvent(InitialPageEvent) },
        onRetryPaginationClicked = { viewModel.processEvent(PaginateEvent) },
        loadNextPage = { viewModel.processEvent(PaginateEvent) }
    )
}

@Composable
fun PhotosListScreen(
    modifier: Modifier = Modifier,
    state: PhotosListUIState,
    onRetryInitialPageClicked: () -> Unit,
    onRetryPaginationClicked: () -> Unit,
    loadNextPage: () -> Unit,
) {

    if (state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

    if (state.error != null) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxHeight()
        ) {
            Text(
                text = state.error,
                textAlign = TextAlign.Center,
            )
            Button(onClick = onRetryInitialPageClicked) {
                Text(text = "Retry")
            }
        }
    }

    if (state.images.isNotEmpty()) {
        PhotosList(
            modifier = modifier,
            items = state.images,
            pageLinks = state.pageLinks,
            paginationError = state.paginationError,
            isPaginationLoading = state.isPaginationLoading,
            onRetryPaginationClicked = onRetryPaginationClicked,
            loadNextPage = loadNextPage,
        )
    }

}

sealed interface Event
object InitialPageEvent : Event
object PaginateEvent : Event