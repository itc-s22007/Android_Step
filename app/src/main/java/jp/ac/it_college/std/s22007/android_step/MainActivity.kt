package jp.ac.it_college.std.s22007.android_step
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.Firebase
import com.google.firebase.initialize
import jp.ac.it_college.std.s22007.android_step.ui.theme.Android_StepTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_StepTheme {
                Navigation()
            }
        }
    }
}


//@Composable
//fun SimpleOutlinedTextFieldSample() {
//    var text by remember { mutableStateOf("") }
//
//    OutlinedTextField(
//        value = text,
//        onValueChange = { text = it },
//        label = { Text("Label") }
//    )
//}

//@Composable
//fun StepCounterDisplays(sensorManager: SensorManager) {
//    var stepCount by remember { mutableIntStateOf(0) }
//
//    DisposableEffect(sensorManager) {
//        val sensorEventListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent?) {
//                event?.let {
//                    if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
//                        val steps = it.values[0].toInt()
//                        stepCount = steps
//                    }
//                }
//            }
//
//            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//        }
//
//        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        sensorManager.registerListener(sensorEventListener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
//
//        onDispose {
//            sensorManager.unregisterListener(sensorEventListener)
//        }
//    }
//    Text(text = "Steps: $stepCount")
//}