package com.example.gyrographs

import android.content.Context
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

val LocalSensorManager = staticCompositionLocalOf<SensorManager> { error("No SensorManager provided") }
val LocalGyroDatabase = staticCompositionLocalOf<GyroDatabaseHelper> { error("No Database provided") }

@Composable
fun ProvideSensorManager(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    CompositionLocalProvider(
        LocalSensorManager provides sensorManager
    ) { content() }
}

@Composable
fun ProvideGyroDatabase(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val databaseHelper = GyroDatabaseHelper.getInstance(context)
    CompositionLocalProvider(
        LocalGyroDatabase provides databaseHelper
    ) { content() }
}
