package dev.punitd.unplashapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import dev.punitd.Semantics
import dev.punitd.Tags
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
        modifier = modifier
            .fillMaxSize()
            .testTag(Tags.ImagesList),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            val unsplashImage = items[index]
            if (index == items.lastIndex && pageLinks?.next != null && !isPaginationLoading
                && paginationError == null
            ) {
                loadNextPage()
            }
            UnsplashItem(unsplashImage)
        }

        if (isPaginationLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(Tags.PaginationLoader),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (paginationError != null) {
            item(key = paginationError) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = paginationError,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = onRetryPaginationClicked,
                        modifier = Modifier.semantics {
                            contentDescription = Semantics.RetryAction
                        },
                    ) {
                        Text(text = "Retry")
                    }
                }

            }
        }
    }
}

