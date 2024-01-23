package jp.ac.it_college.std.s22007.android_step.timer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TimerScene(
    modifier: Modifier,
    onClickHomeButton: () -> Unit = {}
) {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    val laps by remember { mutableStateOf(mutableListOf<Long>()) }
    val lapList by remember { mutableStateOf(emptyList<Long>()) }

    Surface(modifier, color = Color.Black) {
        LaunchedEffect(isRunning) {
            while (true) {
                if (isRunning) {
                    delay(10)
                    elapsedTime += 10L
                } else {
                    break
                }
            }
        }
        Column {
            Button(
                onClick = onClickHomeButton,
                modifier = Modifier
                    .height(70.dp)
                    .width(50.dp)
                    .padding(horizontal = 0.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(
                    text = "＜",
                    color = Color.White,
                    fontSize = 30.sp
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
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                fontSize = 100.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = { isRunning = !isRunning }, modifier = Modifier
                        .padding(5.dp)
                        .size(90.dp)
                ) {
                    Text(if (isRunning) "Stop" else "Start")
                }
                Button(
                    onClick = {
                        if (!isRunning) {
                            elapsedTime = 0
                            laps.clear()
                        }
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(90.dp)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = {
                        if (isRunning) {
                            laps.add(elapsedTime)
                        }
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(90.dp)
                ) {
                    Text("Rap")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(100) }

            Text("LapTime:", color = Color.White, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.Black)
                    .size(300.dp)
                    .padding(horizontal = 60.dp)
                    .verticalScroll(state)
            ) {
                repeat(1) {
                    if (laps.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(
                            horizontalAlignment = Alignment.Start,
                        ) {
                            laps.forEachIndexed { index, lapTime ->
                                Text(
                                    "$index: ${formatTime(lapTime)}",
                                    color = Color.White,
                                    fontSize = 30.sp,
                                )
                            }
                        }
                    }
                }
            }

            if (lapList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn{
                    items(lapList.size) { index ->
                        Text("$index: ${formatTime(lapList[index])}", color = Color.White)
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