package jp.ac.it_college.std.s22007.android_step.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s22007.android_step.ui.theme.Android_StepTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun TimerScene(
    modifier: Modifier,
    onClickHomeButton: () -> Unit = {}
) {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var laps by remember { mutableStateOf(mutableListOf<Long>()) }

    Surface(modifier) {
        LaunchedEffect(isRunning) {
            while (true) {
                if (isRunning) {
                    delay(10)
                    elapsedTime += 10
                } else {
                    break
                }
            }
        }
        Column {
            Button(
                onClick = onClickHomeButton,
                modifier = Modifier
                    .height(48.dp)
                    .width(148.dp)
            ) {
                Text(
                    text = "＜ー",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = formatTime(elapsedTime),
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { isRunning = !isRunning }, modifier = Modifier.padding(5.dp)
                ) {
                    Text(if (isRunning) "Stop" else "Start")
                }
                Button(
                    onClick = {
                        isRunning = false
                        elapsedTime = 0L
                        laps.clear()
                    },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("Reset")
                }
            }
            Button(
                onClick = {
                    laps.add(elapsedTime)
                },
                modifier = Modifier.padding(5.dp)
            ){
                Text("Rap")
            }
            if (laps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Lap times:")
                    laps.forEachIndexed { index, lapTime ->
                        Text("$index: ${formatTime(lapTime)}")
                    }
                }
            }

            LaunchedEffect(Unit){
                while (true){
                    delay(1000)
                    if (isRunning) elapsedTime += 1000L
                    else break
                }
            }
        }
    }
}


@Composable
fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    val milliseconds = millis % 1000 / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
}
@Preview
@Composable
private fun TimerScenePreview() {
    Android_StepTheme {
        TimerScene(Modifier.fillMaxSize()) {
        }
    }
}