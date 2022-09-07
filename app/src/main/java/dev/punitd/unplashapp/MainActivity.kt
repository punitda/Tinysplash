@file:OptIn(ExperimentalCoilApi::class)

package dev.punitd.unplashapp

import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.AndroidEntryPoint
import dev.punitd.unplashapp.screen.photos.PhotosListScreen
import dev.punitd.unplashapp.screen.photos.PhotosListScreenRoute
import dev.punitd.unplashapp.ui.theme.MoleculeUnplashAppTheme

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoleculeUnplashAppTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Home") },
                            actions = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon"
                                    )
                                }
                            }
                        )
                    },
                    content = {
                        PhotosListScreenRoute(
                            modifier = Modifier.padding(it)
                        )
                    }
                )
            }
        }
    }
}
