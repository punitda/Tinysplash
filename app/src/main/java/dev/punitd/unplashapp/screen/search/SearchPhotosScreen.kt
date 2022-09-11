package dev.punitd.unplashapp.screen.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.ramcosta.composedestinations.annotation.Destination
import dev.punitd.unplashapp.MainNavGraph
import dev.punitd.unplashapp.components.PhotosList

@MainNavGraph
@Destination(route = "search")
@Composable
fun SearchPhotosScreenRoute(modifier: Modifier = Modifier) {
    val viewModel: SearchPhotosViewModel = hiltViewModel()
    val state by viewModel.stateFlow.collectAsState()
    SearchPhotosScreen(
        modifier = modifier,
        state = state,
        onTextChange = { viewModel.processEvent(SearchEvent(it)) },
        onClearSearchClicked = { viewModel.processEvent(ClearSearchEvent) },
        onRetryPaginationClicked = { viewModel.processEvent(PaginateEvent) },
        loadNextPage = { viewModel.processEvent(PaginateEvent) },
    )
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchPhotosScreen(
    modifier: Modifier = Modifier,
    state: SearchUIState,
    onTextChange: (String) -> Unit,
    onClearSearchClicked: () -> Unit,
    onRetryPaginationClicked: () -> Unit,
    loadNextPage: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchWidget(
                text = state.currentQuery,
                isSuggestionLoading = state.isLoading,
                onTextChange = onTextChange,
                onClearClicked = onClearSearchClicked,
            )
        },
        content = {
            PhotosList(
                modifier = Modifier.padding(it),
                items = state.images,
                pageLinks = state.pageLinks,
                paginationError = state.paginationError,
                isPaginationLoading = state.isPaginationLoading,
                loadNextPage = loadNextPage,
                onRetryPaginationClicked = onRetryPaginationClicked,
            )
        }
    )
}

sealed interface Event
data class SearchEvent(val query: String) : Event
object ClearSearchEvent : Event
object PaginateEvent : Event