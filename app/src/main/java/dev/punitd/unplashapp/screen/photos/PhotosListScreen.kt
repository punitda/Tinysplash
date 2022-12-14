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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import dev.punitd.Semantics
import dev.punitd.Tags
import dev.punitd.unplashapp.MainNavGraph
import dev.punitd.unplashapp.R
import dev.punitd.unplashapp.components.PhotosList

@MainNavGraph(start = true)
@Destination(route = "photos_list")
@Composable
fun PhotosListScreenRoute() {
    val viewModel: PhotosListViewModel = hiltViewModel()
    val state by viewModel.stateFlow.collectAsState()
    PhotosListScreen(
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
            CircularProgressIndicator(modifier = Modifier.testTag(Tags.ScreenLoader))
        }
    }

    if (state.error != null) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = state.error,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = onRetryInitialPageClicked,
                modifier = Modifier.semantics { contentDescription = Semantics.RetryAction },
            ) {
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