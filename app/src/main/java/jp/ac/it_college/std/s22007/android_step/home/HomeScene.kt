package jp.ac.it_college.std.s22007.android_step.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.it_college.std.s22007.android_step.R
import jp.ac.it_college.std.s22007.android_step.ui.theme.Android_StepTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScene(
    modifier: Modifier = Modifier,
    onClickTimerButton: () -> Unit = {},
    onClickMapButton: () -> Unit = {}
) {
    var currentDate by remember { mutableStateOf(getCurrentDate()) }
    var currentData by remember { mutableStateOf("") }
    val currentLocalTime = remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentLocalTime.value = LocalTime.now()
            delay(1000)
        }
    }
    Surface(modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val previousDate = getPreviousDate(currentDate)
                        currentData = fetchDataForDate(previousDate)
                        currentDate = previousDate

                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("＜")
                }

                Text(
                    text = "$currentDate",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.displaySmall
                )

                Button(
                    onClick = {
                        currentData = fetchDataForDate(getCurrentDate())
                        currentDate = getCurrentDate()
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("＞")
                }
            }
            Text(
                text = "${currentLocalTime.value.hour}:${currentLocalTime.value.minute}",
                style = MaterialTheme.typography.displaySmall
            )
            val circleAngle = 360f
            val max = 300f
            val angle = 240f
            val progress = 200f
            val progressWidth = 14.dp
            val backGroundWidth = 20.dp
            val startAngle = (circleAngle / 4) + ((circleAngle - angle) / 2)
            Canvas(
                modifier = Modifier
                    .size(400.dp)
                    .padding(20.dp),
                onDraw = {
                    drawArc(
                        color = Color.Black,
                        startAngle = startAngle,
                        sweepAngle = angle,
                        useCenter = false,
                        style = Stroke(
                            width = backGroundWidth.toPx(),
                            cap = StrokeCap.Round
                        ),
                        size = Size(size.width, size.height),
                    )
                    drawArc(
                        color = Color.DarkGray,
                        startAngle = startAngle,
                        sweepAngle = angle,
                        useCenter = false,
                        style = Stroke(width = progressWidth.toPx(), cap = StrokeCap.Round),
                        size = Size(size.width, size.height)
                    )
                    drawArc(
                        color = Color.Cyan,
                        startAngle = startAngle,
                        sweepAngle = angle / max * progress,
                        useCenter = false,
                        style = Stroke(width = progressWidth.toPx(), cap = StrokeCap.Round),
                        size = Size(size.width, size.height)
                    )
                }
            )

            Row {
                Button(onClick = onClickTimerButton, modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = stringResource(id = R.string.to_timer))
                }
                Button(onClick = onClickMapButton, modifier = Modifier.padding(5.dp)
                ){
                    Text(text = stringResource(id = R.string.to_Map))
                }
                Button(onClick = {}, modifier = Modifier.padding(5.dp)
                ){
                    Text("share")
                }
            }
        }
    }
}


fun fetchDataForDate(date: LocalDate): String {
    return "データ: $date"
}

fun getCurrentDate(): LocalDate {
    return LocalDate.now()
}

fun getPreviousDate(currentDate: LocalDate): LocalDate {
    return currentDate.minusDays(1)
}

@Preview
@Composable
private fun HomeScenePreview() {
    Android_StepTheme {
        HomeScene()
    }
}