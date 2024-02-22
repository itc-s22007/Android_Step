package jp.ac.it_college.std.s22007.android_step

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavViewModelStoreProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s22007.android_step.home.HomeScene
import jp.ac.it_college.std.s22007.android_step.map.Map
import jp.ac.it_college.std.s22007.android_step.stopwatch.StopWatchScene
import jp.ac.it_college.std.s22007.android_step.timer.TimerScene
import java.time.LocalTime

object Destination {
    const val HOME = "home"
    const val STOPWATCH = "stopwatch"
    const val TIMER = "timer"
    const val MAP = "map"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
) {
    var stopwatchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stopwatchText)
            })
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.HOME,
            modifier = Modifier.padding(it)
        ){
            composable(Destination.HOME){
                HomeScene(
                    onClickStopButton = {
                        navController.navigate(Destination.STOPWATCH)
                    },
                    onClickTimerButton = {
                        navController.navigate(Destination.TIMER)
                    },
                    onClickMapButton = {
                        navController.navigate(Destination.MAP)
                    }
                )
            }
            composable(Destination.TIMER){
                TimerScene(
                    modifier = Modifier.fillMaxSize(),
                    onClickHomeButton = {
                        navController.navigate(Destination.HOME)
                    }
                )
            }
            composable(Destination.STOPWATCH){
                StopWatchScene(
                    modifier = Modifier.fillMaxSize(),
                    onClickHomeButton = {
                        navController.navigate(Destination.HOME)
                    },
                    onClickTimerButton = {
                        navController.navigate(Destination.TIMER)
                    }
                )
            }

            composable(Destination.MAP){
                Map()
            }
        }
    }
}