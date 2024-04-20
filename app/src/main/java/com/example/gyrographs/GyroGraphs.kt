package com.example.gyrographs

import android.content.Intent
import android.hardware.Sensor
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.hardware.SensorManager
import androidx.compose.runtime.*

@Composable
fun GyroGraphs() {
    var xOrientation by remember { mutableStateOf(0f) }
    var yOrientation by remember { mutableStateOf(0f) }
    var zOrientation by remember { mutableStateOf(0f) }
    var sensorInterval by remember { mutableStateOf(SensorManager.SENSOR_DELAY_NORMAL) }

    val sensorManager = LocalSensorManager.current
    val databaseHelper = LocalGyroDatabase.current
    val sensorListener = remember {
        GyroSensorListener({ x, y, z ->
            xOrientation = x
            yOrientation = y
            zOrientation = z
        }, databaseHelper)
    }

    DisposableEffect(sensorManager) {
        sensorManager.registerListener(
            sensorListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            sensorInterval
        )
        onDispose { sensorManager.unregisterListener(sensorListener) }
//        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GyroGraphs",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Text(text = "X Axis: ${xOrientation}°")
        Text(text = "Y Axis: ${yOrientation}°")
        Text(text = "Z Axis: ${zOrientation}°")
        SensorIntervalButtons(onIntervalChange = { newInterval ->
            sensorInterval = newInterval
            sensorManager.unregisterListener(sensorListener)
            sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorInterval
            )
        })
        Text(
            text = "Current Sensor Interval: ${getIntervalName(sensorInterval)}",
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        val context = LocalContext.current

        Button(
            onClick = {
                val intent = Intent(context, OrientationHistoryActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text("View Orientation History")
        }
    }
}

fun getIntervalName(interval: Int): String {
    return when (interval) {
        SensorManager.SENSOR_DELAY_NORMAL -> "Normal Interval"
        SensorManager.SENSOR_DELAY_UI -> "UI Interval"
        SensorManager.SENSOR_DELAY_GAME -> "Game Interval"
        else -> "Unknown"
    }
}

@Composable
fun SensorIntervalButtons(onIntervalChange: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onIntervalChange(SensorManager.SENSOR_DELAY_NORMAL) },
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text("Normal Interval")
        }
        Button(
            onClick = { onIntervalChange(SensorManager.SENSOR_DELAY_UI) },
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text("UI Interval")
        }
        Button(
            onClick = { onIntervalChange(SensorManager.SENSOR_DELAY_GAME) },
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text("Game Interval")
        }
    }
}
