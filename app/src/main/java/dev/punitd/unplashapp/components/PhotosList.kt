package dev.punitd.unplashapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import dev.punitd.unplashapp.model.PageLinks
import dev.punitd.unplashapp.model.UnsplashImage

@ExperimentalCoilApi
@Composable
fun PhotosList(
    modifier: Modifier = Modifier,
    items: List<UnsplashImage>,
    pageLinks: PageLinks? = null,
    paginationError: String? = null,
    isPaginationLoading: Boolean = false,
    loadNextPage: () -> Unit,
    onRetryPaginationClicked: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            val unsplashImage = items[index]
            if (index == items.lastIndex && pageLinks?.next != null && !isPaginationLoading) {
                loadNextPage()
            }
            UnsplashItem(unsplashImage)
        }

        if (isPaginationLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        if (paginationError != null) {
            item(key = { paginationError }) {
                Text(
                    text = paginationError,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable(onClick = onRetryPaginationClicked)
                )
            }
        }
    }
}

