package jp.ac.it_college.std.s22007.android_step

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.ac.it_college.std.s22007.android_step.home.HomeScene
import jp.ac.it_college.std.s22007.android_step.timer.TimerScene

object Destination {
    const val Home = "home"
    const val TIMER = "timer"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
) {
    var timerText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = timerText)
            })
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.Home,
            modifier = Modifier.padding(it)
        ){
            composable(Destination.Home){
                HomeScene(
                    onClickTimerButton = {
                        navController.navigate(Destination.TIMER)
                    },
                )
            }
            composable(Destination.TIMER){
                TimerScene(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    navController.navigate(Destination.Home)
                }
            }
        }
    }
}