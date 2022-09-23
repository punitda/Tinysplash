package dev.punitd.unplashapp.screen.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.punitd.Semantics
import dev.punitd.Tags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWidget(
    isSuggestionLoading: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    onClearClicked: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.primary,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = Semantics.SearchInput
                },
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(alpha = DefaultAlpha),
                    text = "Search here..",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (isSuggestionLoading) {
                    CircularProgressIndicator(
                        Modifier
                            .size(24.dp)
                            .testTag(Tags.SearchSuggestionsLoader)
                    )
                } else if (text.isNotBlank()) {
                    IconButton(
                        onClick = onClearClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = Semantics.ClearInput
                        )
                    }
                }
            }
        )
    }
}