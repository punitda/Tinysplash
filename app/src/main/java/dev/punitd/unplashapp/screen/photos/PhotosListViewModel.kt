package dev.punitd.unplashapp.screen.photos

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.punitd.unplashapp.data.UnsplashRepository
import dev.punitd.unplashapp.model.Error
import dev.punitd.unplashapp.model.PageLinks
import dev.punitd.unplashapp.model.Success
import dev.punitd.unplashapp.model.UnsplashImage
import dev.punitd.unplashapp.util.Constants.ITEM_PER_PAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosListViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    private val events = MutableSharedFlow<Event>()
    val stateFlow = scope.launchMolecule(clock = RecompositionClock.ContextClock) {
        present(events)
    }

    init {
        processEvent(InitialPageEvent)
    }

    @Composable
    private fun present(events: Flow<Event>): PhotosListUIState {
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }
        var isPaginationLoading by remember { mutableStateOf(false) }
        var paginationError by remember { mutableStateOf<String?>(null) }
        val images = remember { mutableStateListOf<UnsplashImage>() }
        var pageLinks by remember { mutableStateOf<PageLinks?>(null) }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    InitialPageEvent -> {
                        isLoading = true
                        error = null
                        when (val result =
                            unsplashRepository.getPhotos(page = 1, perPage = ITEM_PER_PAGE)) {
                            is Error -> {
                                isLoading = false
                                error = result.message
                            }
                            is Success -> {
                                isLoading = false
                                images.addAll(result.data.images)
                                pageLinks = result.data.pageLinks
                            }
                        }

                    }
                    is PaginateEvent -> {
                        if (isPaginationLoading) return@collect
                        val nextPageUrl = pageLinks?.next ?: return@collect
                        isPaginationLoading = true
                        paginationError = null
                        when (val result = unsplashRepository.getPhotosByUrl(nextPageUrl)) {
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
                }
            }
        }
        return PhotosListUIState(
            isLoading = isLoading,
            error = error,
            images = images,
            pageLinks = pageLinks,
            isPaginationLoading = isPaginationLoading,
            paginationError = paginationError,
        )
    }

    fun processEvent(event: Event) {
        scope.launch {
            events.emit(event)
        }
    }
}

data class PhotosListUIState(
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val error: String? = null,
    val paginationError: String? = null,
    val images: List<UnsplashImage> = emptyList(),
    val pageLinks: PageLinks? = null,
)