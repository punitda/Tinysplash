package dev.punitd.unplashapp.screen.search

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.punitd.unplashapp.data.UnsplashRepository
import dev.punitd.unplashapp.di.FrameClock
import dev.punitd.unplashapp.di.MoleculeScope
import dev.punitd.unplashapp.model.Error
import dev.punitd.unplashapp.model.PageLinks
import dev.punitd.unplashapp.model.Success
import dev.punitd.unplashapp.model.UnsplashImage
import dev.punitd.unplashapp.util.Constants.ITEM_PER_PAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPhotosViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    @MoleculeScope private val scope: CoroutineScope,
    @FrameClock private val clock: RecompositionClock,
) : ViewModel() {
    companion object {
        const val SEARCH_TIMEOUT = 300L
    }

    private val events = MutableSharedFlow<Event>()
    val stateFlow = scope.launchMolecule(clock = clock) {
        present(events)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    @Composable
    fun present(events: Flow<Event>): SearchUIState {
        var isSuggestionsLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }
        var isPaginationLoading by remember { mutableStateOf(false) }
        var paginationError by remember { mutableStateOf<String?>(null) }
        val images = remember { mutableStateListOf<UnsplashImage>() }
        var pageLinks by remember { mutableStateOf<PageLinks?>(null) }
        var currentQuery by remember { mutableStateOf("") }


        // Search Query
        LaunchedEffect(Unit) {
            events
                .filter(::filterSearchEvent)
                .map(::mapToSearchEvent)
                .onEach {
                    currentQuery = it.query
                    isSuggestionsLoading = true
                }
                .debounce(SEARCH_TIMEOUT)
                .flatMapLatest {
                    unsplashRepository.search(
                        query = it.query,
                        perPage = ITEM_PER_PAGE
                    )
                }
                .collect { result ->
                    when (result) {
                        is Error -> {
                            isSuggestionsLoading = false
                            error = result.message
                        }
                        is Success -> {
                            isSuggestionsLoading = false
                            images.clear()
                            images.addAll(result.data.images)
                            pageLinks = result.data.pageLinks
                        }
                    }
                }
        }

        // Paginate search results
        LaunchedEffect(Unit) {
            events
                .collect { event ->
                    when (event) {
                        ClearSearchEvent -> {
                            currentQuery = ""
                        }
                        PaginateEvent -> {
                            if (isPaginationLoading) return@collect
                            val nextPageUrl = pageLinks?.next ?: return@collect
                            isPaginationLoading = true
                            paginationError = null
                            when (
                                val result = unsplashRepository.searchPhotosByUrl(nextPageUrl)
                            ) {
                                is Error -> {
                                    isPaginationLoading = false
                                    paginationError = result.message
                                }
                                is Success -> {
                                    isPaginationLoading = false
                                    images.addAll(result.data.images)
                                    pageLinks = result.data.pageLinks
                                }
                            }


                        }
                        is SearchEvent -> {

                        }
                    }
                }
        }


        return SearchUIState(
            isLoading = isSuggestionsLoading,
            isPaginationLoading = isPaginationLoading,
            error = error,
            paginationError = paginationError,
            images = images.toList(),
            pageLinks = pageLinks,
            currentQuery = currentQuery,
        )
    }

    private fun filterSearchEvent(event: Event): Boolean {
        if (event is SearchEvent) {
            return event.query.isNotEmpty() && event.query.trim().isNotBlank()
        }
        return false
    }

    private fun mapToSearchEvent(event: Event) = event as SearchEvent

    fun processEvent(event: Event) {
        scope.launch {
            events.emit(event)
        }
    }
}

data class SearchUIState(
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val error: String? = null,
    val paginationError: String? = null,
    val images: List<UnsplashImage> = emptyList(),
    val pageLinks: PageLinks? = null,
    val currentQuery: String = "",
)