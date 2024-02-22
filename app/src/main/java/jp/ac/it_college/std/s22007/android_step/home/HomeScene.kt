package jp.ac.it_college.std.s22007.android_step.home

import android.content.Context
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.core.content.ContextCompat.startActivity
import jp.ac.it_college.std.s22007.android_step.R
import jp.ac.it_college.std.s22007.android_step.sns.shereIntent
import jp.ac.it_college.std.s22007.android_step.ui.theme.Android_StepTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScene(
    modifier: Modifier = Modifier,
    onClickStopButton: () -> Unit = {},
    onClickMapButton: () -> Unit = {},
    onClickTimerButton: () -> Unit = {},
) {
    var currentDate by remember { mutableStateOf(getCurrentDate()) }
    val currentLocalTime = remember { mutableStateOf(LocalTime.now()) }
    var goalSteps by remember { mutableIntStateOf(0) }
    var isGoalDialogVisible by remember { mutableStateOf(false) }
    val stepCount = remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    var previousStepCount by remember { mutableStateOf(0) }
    val today = getCurrentDate()
    if (today != currentDate) {
        previousStepCount = stepCount.value
        currentDate = today
    }

        LaunchedEffect(Unit) {
            while (true) {
                currentLocalTime.value = LocalTime.now()
                delay(1000)
            }
        }
    Surface(modifier, color = Color.Black) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$currentDate",
                    fontSize = 40.sp,
                    modifier = modifier.width(210.dp),
                    textAlign = TextAlign.Center,
                    color = White
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${currentLocalTime.value.hour}:${currentLocalTime.value.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.displaySmall,
                    color = White,
                )
                if (isGoalDialogVisible) {
                    GoalInputDialog(
                        onNewGoalSet = { newGoal -> goalSteps = newGoal },
                        onDismiss = { isGoalDialogVisible = false }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            val circleAngle = 360f
            val max = 400f
            val angle = 400f
            val progress = 400f
            val progressWidth = 14.dp
            val backGroundWidth = 20.dp
            val startAngle = (circleAngle / 4) + ((circleAngle - angle) / 2)
            val textColor = if (stepCount.value >= goalSteps) Color.Green else Color.White
            StepCounterDisplays(stepCount, goalSteps) // goalStepsを渡す
            Canvas(
                modifier = Modifier
                    .size(340.dp),
                onDraw = {
                    val centerX = size.width / 2
                    val centerx = size.width / 2
                    val centerY = size.height / 4
                    val text = "Today's,goal！"
                    val text2 = "${stepCount.value} / $goalSteps"
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
                    drawContext.canvas.nativeCanvas.drawText("${stepCount.value} / $goalSteps", centerx, centerY + 300, Paint().apply {
                        color = textColor.toArgb()
                        textSize = 120f
                        textAlign = Paint.Align.CENTER
                    })
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
            Spacer(modifier = Modifier.padding(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val steps = stepCount.value
                val distanceInMeters = calculateDistanceFromStepsAndHeight(steps, 175)
                val burnedCalories = calculateCalories(steps, 0f).toInt()

                Text(
                    text = String.format("%.1f", distanceInMeters / 10),
                    fontSize = 30.sp,
                    modifier = Modifier.width(120.dp),
                    textAlign = TextAlign.Center,
                    color = White
                )

                Text(
                    text = "$burnedCalories",
                    fontSize = 30.sp,
                    modifier = Modifier.width(120.dp),
                    textAlign = TextAlign.Center,
                    color = White
                )
            }

            Row {
                Button(
                    onClick = onClickTimerButton, modifier = Modifier.padding(5.dp)
                Text(
                    text = "Km",
                    fontSize = 30.sp,
                    modifier = modifier.width(120.dp),
                    textAlign = TextAlign.Center,
                    color = White
                )
                Text(
                    text = "Kcal",
                    fontSize = 30.sp,
                    modifier = modifier.width(120.dp),
                    textAlign = TextAlign.Center,
                    color = White
                )
            }
            Row (
                modifier = modifier.padding(5.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                Button(onClick = onClickStopButton, modifier = Modifier
                    .padding(5.dp)
                    .size(90.dp),
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
                    colors = ButtonDefaults.buttonColors(White)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.map1),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(text = stringResource(id = R.string.to_Map))
                }
                Button(
                    onClick = { startActivity(context, shereIntent, null) },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(90.dp),
                    colors = ButtonDefaults.buttonColors(White)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row (
                modifier = modifier.padding(5.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                Button(onClick = onClickTimerButton, modifier = Modifier
                    .padding(5.dp)
                    .size(90.dp),
                    colors = ButtonDefaults.buttonColors(White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tim),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                    )
                    Text(text = stringResource(id = R.string.to_timer))
                }
                Button(
                    onClick = { isGoalDialogVisible = true },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(90.dp),
                    colors = ButtonDefaults.buttonColors(White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                    )
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
            TextField(
                value = newGoal,
                onValueChange = {
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
            Button(
                onClick = {
                    onNewGoalSet(newGoal.toInt())
                    onDismiss()
                }
            ) {
                Text("Set Goal")
            }
        },
        dismissButton = {
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

fun calculateDistanceFromStepsAndHeight(steps: Int, heightInCm: Int): Float {
    val averageStepLengthInMeters = heightInCm * 0.415 / 100
    return steps * averageStepLengthInMeters.toFloat()
}



@Composable
fun StepCounterDisplays(
    steps: MutableState<Int>,
    goalSteps: Int
) {
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
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

fun getCurrentDate(): LocalDate {
    return LocalDate.now()
}


@Preview
@Composable
private fun HomeScenePreview() {
    Android_StepTheme {
        HomeScene()
    }
}
