package dev.punitd.unplashapp.screen.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.ramcosta.composedestinations.annotation.Destination
import dev.punitd.unplashapp.MainNavGraph
import dev.punitd.unplashapp.components.PhotosList
import kotlinx.coroutines.launch

@MainNavGraph
@Destination(route = "search")
@Composable
fun SearchPhotosScreenRoute() {
    val viewModel: SearchPhotosViewModel = hiltViewModel()
    val state by viewModel.stateFlow.collectAsState()
    SearchPhotosScreen(
        state = state,
        onTextChange = { viewModel.processEvent(SearchEvent(it)) },
        onClearSearchClicked = { viewModel.processEvent(ClearSearchEvent) },
        onRetryPaginationClicked = { viewModel.processEvent(PaginateEvent) },
        loadNextPage = { viewModel.processEvent(PaginateEvent) },
        onResetError = { viewModel.processEvent(ResetErrorEvent) }
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
    onResetError: () -> Unit,
) {
    // Keeping this state locally because of clock sync issues when using TextField with
    // Molecule's scheduler
    // Refer issue : https://github.com/cashapp/molecule/issues/63
    var searchText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.error, block = {
        if (state.error != null) {
            scope.launch {
                snackbarHostState.showSnackbar(state.error)
                onResetError()
            }
        }
    })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
        topBar = {
            SearchWidget(
                text = searchText,
                isSuggestionLoading = state.isLoading,
                onTextChange = {
                    searchText = it
                    onTextChange(it)
                },
                onClearClicked = {
                    searchText = ""
                    onClearSearchClicked()
                },
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
object ResetErrorEvent : Event