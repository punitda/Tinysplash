package dev.punitd.unplashapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.RootNavGraph
import dagger.hilt.android.AndroidEntryPoint
import dev.punitd.unplashapp.components.BottomBar
import dev.punitd.unplashapp.screen.NavGraphs
import dev.punitd.unplashapp.ui.theme.MoleculeUnplashAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoleculeUnplashAppTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }) {
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.main,
            modifier = Modifier.padding(it),
        )
    }
}


@RootNavGraph(start = true)
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class MainNavGraph(val start: Boolean = false)
