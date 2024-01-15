package jp.ac.it_college.std.s22007.android_step.home

import android.content.Context
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import jp.ac.it_college.std.s22007.android_step.R
import jp.ac.it_college.std.s22007.android_step.ui.theme.Android_StepTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScene(
    modifier: Modifier = Modifier,
    onClickTimerButton: () -> Unit = {},
    onClickMapButton: () -> Unit = {},
) {
    var currentDate by remember { mutableStateOf(getCurrentDate()) }
    var currentData by remember { mutableStateOf("") }
    val currentLocalTime = remember { mutableStateOf(LocalTime.now()) }
    var goalSteps by remember { mutableIntStateOf(0) }
    var isGoalDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            currentLocalTime.value = LocalTime.now()
            delay(1000)
        }
    }
    Surface(modifier, color = Color.Black) {
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
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = isButtonEnabled(currentDate),
                    colors = ButtonDefaults.buttonColors(Transparent)
                ) {
                    Text(
                        "＜",
                        fontSize = 30.sp
                    )
                }
                    Text(
                        text = "$currentDate",
                        fontSize = 40.sp,
                        modifier = modifier.width(210.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Button(
                        onClick = {
                            val nextDate = getNextDate(currentDate)
                            currentData = fetchDataForDate(nextDate)
                            currentDate = nextDate
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(Transparent)
                    ) {
                        Text(
                            "＞",
                            fontSize = 30.sp
                        )
                    }
                }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(90.dp))
                Text(
                    text = "${currentLocalTime.value.hour}:${currentLocalTime.value.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                )

                Button(
                    onClick = {
                              isGoalDialogVisible = true
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .size(60.dp)
                        .width(10.dp),
                    colors = ButtonDefaults.buttonColors(Gray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Set Goal",
                        modifier = Modifier.size(50.dp),
                    )
                }
                if (isGoalDialogVisible) {
                    GoalInputDialog(
                        onNewGoalSet = { newGoal -> goalSteps = newGoal },
                        onDismiss = { isGoalDialogVisible = false }
                    )
                }

            }
            val circleAngle = 360f
            val max = 400f
            val angle = 300f
            val progress = 400f
            val progressWidth = 14.dp
            val backGroundWidth = 20.dp
            val startAngle = (circleAngle / 4) + ((circleAngle - angle) / 2)
            val stepCount = remember { mutableIntStateOf(0) }
//            StepCounterDisplays(stepCount)
            Canvas(
                modifier = Modifier
                    .size(370.dp)
                    .padding(5.dp),
                onDraw = {
                    val centerX = size.width / 2
                    val centerx = size.width / 2
                    val centerY = size.height / 4
                    val text = "Today's,goal！"
                    val text2 = "${stepCount.intValue} / $goalSteps"
                    val text3 = "Steps"
                    val paint = Paint().apply {
                        color = android.graphics.Color.LTGRAY
                        textSize = 100f
                        textAlign = Paint.Align.CENTER
                    }
                    val paint2 = Paint().apply {
                        color = android.graphics.Color.LTGRAY
                        textSize = 120f
                        textAlign = Paint.Align.CENTER
                    }
                    drawContext.canvas.nativeCanvas.drawText(text, centerX, centerY + 50, paint)
                    drawContext.canvas.nativeCanvas.drawText(text2, centerx, centerY + 300, paint2)
                    drawContext.canvas.nativeCanvas.drawText(text3, centerx, centerY + 550, paint)
                    drawContext.canvas.nativeCanvas.drawText("${stepCount.intValue} / $goalSteps", centerx, centerY + 300, paint2)
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
                Text(
                    text = "${stepCount.intValue}",
                    fontSize = 40.sp,
                    modifier = modifier
                        .width(120.dp)
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
                val steps = stepCount.intValue
                val distanceInMeters = 0f
                val burnedCalories = calculateCalories(steps, distanceInMeters).toInt()
                Text(
                    text = "$burnedCalories",
                    fontSize = 40.sp,
                    modifier = modifier
                        .width(120.dp)
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
            Row {
                Text(text = "Km",
                    fontSize = 30.sp,
                    modifier = modifier.width(120.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(text = "Kcal",
                    fontSize = 30.sp,
                    modifier = modifier.width(120.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

            Row (
                modifier = modifier.padding(30.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                Button(onClick = onClickTimerButton, modifier = Modifier
                    .padding(5.dp)
                    .size(90.dp),
//                    colors = ButtonDefaults.buttonColors(White)
                    colors = ButtonDefaults.buttonColors(White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.taimer),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                    )
                    Text(text = stringResource(id = R.string.to_timer))
                }
                Button(onClick = onClickMapButton, modifier = Modifier
                    .padding(5.dp)
                    .size(90.dp),
//                    colors = ButtonDefaults.buttonColors(White)
                    colors = ButtonDefaults.buttonColors(White)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.map1),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                   Text(text = stringResource(id = R.string.to_Map))
                }
                Button(onClick = {}, modifier = Modifier
                    .padding(5.dp)
                    .size(90.dp),
//                    colors = ButtonDefaults.buttonColors(White)
                    colors = ButtonDefaults.buttonColors(White)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
//                    Text("share", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun GoalInputDialog(
    onNewGoalSet: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var newGoal by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Set Goal") },
        text = {
            // Use TextField to get numerical input
            TextField(
                value = newGoal,
                onValueChange = {
                    // Handle value change and ensure it is a valid integer
                    if (it.isNotEmpty() && it.isDigitsOnly()) {
                        newGoal = it
                    }
                },
                label = { Text("Enter steps") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
        },
        confirmButton = {
            // Button to confirm the entered goal
            Button(
                onClick = {
                    onNewGoalSet(newGoal.toInt())
                    // Dismiss the dialog
                    onDismiss()
                }
            ) {
                Text("Set Goal")
            }
        },
        dismissButton = {
            // Button to dismiss the dialog
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun StepCounterDisplays(steps: MutableState<Int>) {
    val sensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    DisposableEffect(sensorManager) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                        val count = it.values[0].toInt()
                        steps.value = count
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(
            sensorEventListener,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
}

@Composable
fun calculateCalories(steps: Int, distanceInMeters: Float): Double {
    val caloriesPerStep = 0.05
    val caloriesFromDistance = distanceInMeters * 0.1
    return (steps * caloriesPerStep) + caloriesFromDistance
}

@Composable
fun isButtonEnabled(currentDate: LocalDate): Boolean {
    val sevenDaysAgo = LocalDate.now().minusDays(7)
    return currentDate > sevenDaysAgo
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

fun getNextDate(currentDate: LocalDate): LocalDate {
    val tomorrow = LocalDate.now().plusDays(0)
    return if (currentDate >= LocalDate.now()) tomorrow else currentDate.plusDays(1)
}


@Preview
@Composable
private fun HomeScenePreview() {
    Android_StepTheme {
        HomeScene()
    }
}
