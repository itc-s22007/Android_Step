package jp.ac.it_college.std.s22007.android_step.timer

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s22007.android_step.ui.theme.Android_StepTheme
import kotlinx.coroutines.delay
import java.time.Duration

@Composable
fun TimerScene(
    modifier: Modifier,
    onClickStopButton: () -> Unit = {}
) {
    var selectedMinutes by remember { mutableStateOf(0) }
    var selectedSeconds by remember { mutableStateOf(0) }
    val mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var countDownDuration by remember { mutableStateOf(Duration.ZERO) }
    var isTimerRunning by remember { mutableStateOf(false) }
    val minutesTextFieldState = remember { mutableStateOf(TextFieldValue("")) }
    val secondsTextFieldState = remember { mutableStateOf(TextFieldValue("")) }
    val savedTimes = remember { mutableStateOf(listOf<Pair<Int, Int>>()) }

    Surface(modifier, color = Color.Black) {
        Column {
            Button(
                onClick = onClickStopButton,
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier
            ) {
                Text(
                    "＜",
                    fontSize = 30.sp,
                    color = Color.White
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val minutes = countDownDuration.toMinutes()
            val seconds = (countDownDuration.toMillis() / 1000) - (minutes * 60)

            Text(
                text = "${if (minutes < 10) "0" else ""}$minutes:${if (seconds < 10) "0" else ""}$seconds",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xff979494),
                fontWeight = FontWeight.Medium,
                fontSize = 90.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = minutesTextFieldState.value,
                    onValueChange = {
                        val newValue = it.text.toIntOrNull() ?: 0
                        selectedMinutes = newValue.coerceIn(0, 99)
                        minutesTextFieldState.value = TextFieldValue(selectedMinutes.toString())
                    },
                    label = { Text("Minutes") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(100.dp)
                )
                Text("：", color = Color.White, fontSize = 30.sp)
                OutlinedTextField(
                    value = secondsTextFieldState.value,
                    onValueChange = {
                        val newValue = it.text.toIntOrNull() ?: 0
                        selectedSeconds = newValue.coerceIn(0, 59)
                        secondsTextFieldState.value = TextFieldValue(selectedSeconds.toString())
                    },
                    label = { Text("Seconds") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                        }
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .width(100.dp)
                )
            }
            Row {
                Button(
                    onClick = {
                        savedTimes.value = savedTimes.value + Pair(selectedMinutes, selectedSeconds)
                    },
                    modifier = Modifier.padding(15.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text("保存", color = Color.White)
                }
                Button(
                    onClick = {
                        if (isTimerRunning) {
                            // タイマーが動作中の場合はタイマーをリセットする
                            countDownDuration = Duration.ZERO
                            isTimerRunning = false // タイマーが停止状態にする
                        } else {
                            // タイマーが停止している場合は保存した時間をリセットする
                            savedTimes.value = emptyList()
                        }
                    },
                    modifier = Modifier.padding(15.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text("リセット", color = Color.White)
                }
                Button(
                    onClick = {
                        isTimerRunning = !isTimerRunning
                    },
                    modifier = Modifier.padding(15.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    enabled = savedTimes.value.isNotEmpty()
                ) {
                    Text(if (isTimerRunning) "ストップ" else "開始", color = Color.White)
                }
                LaunchedEffect(isTimerRunning) {
                    if (isTimerRunning) {
                        for (time in savedTimes.value) {
                            selectedMinutes = time.first
                            selectedSeconds = time.second
                            countDownDuration = Duration.ofMinutes(selectedMinutes.toLong())
                                .plusSeconds(selectedSeconds.toLong())
                            while (isTimerRunning && countDownDuration.toMillis() > 0) {
                                delay(1000)
                                countDownDuration = countDownDuration.minusMillis(1000)
                            }
                            if (countDownDuration.toMillis() <= 0) {
                                mediaPlayer?.start()
                            }
                        }
                    }
                }

            }
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(100) }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.Black)
                    .size(300.dp)
                    .padding(horizontal = 60.dp)
                    .verticalScroll(state)
            ) {
                savedTimes.value.reversed().forEachIndexed { index, time ->
                    Text(
                        "${index + 1} : ${time.first} 分 ${time.second} 秒",
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimerScenePreview() {
    Android_StepTheme {
        TimerScene(Modifier.fillMaxSize())
    }
}




