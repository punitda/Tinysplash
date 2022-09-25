package dev.punitd.unplashapp.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.punitd.Tags
import dev.punitd.unplashapp.R
import dev.punitd.unplashapp.screen.NavGraphs
import dev.punitd.unplashapp.screen.appCurrentDestinationAsState
import dev.punitd.unplashapp.screen.destinations.Destination
import dev.punitd.unplashapp.screen.destinations.PhotosListScreenRouteDestination
import dev.punitd.unplashapp.screen.destinations.SearchPhotosScreenRouteDestination
import dev.punitd.unplashapp.screen.startAppDestination

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination: Destination =
        navController.appCurrentDestinationAsState().value
            ?: NavGraphs.root.startAppDestination

    NavigationBar {
        BottomBarDestination.values().forEach { destination ->
            NavigationBarItem(
                modifier = Modifier.testTag(destination.testTag),
                selected = currentDestination == destination.direction,
                onClick = {
                    if (currentDestination == destination.direction) {
                        navController.popBackStack(destination.direction, false)
                        return@NavigationBarItem
                    }

                    navController.navigate(destination.direction) {
                        popUpTo(NavGraphs.main.startRoute) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            )
        }
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int,
    val testTag: String,
) {
    Photos(
        PhotosListScreenRouteDestination,
        Icons.Default.Home,
        R.string.home_screen,
        Tags.PhotosTab
    ),
    Search(
        SearchPhotosScreenRouteDestination,
        Icons.Default.Search,
        R.string.search_screen,
        Tags.SearchTab
    )
}