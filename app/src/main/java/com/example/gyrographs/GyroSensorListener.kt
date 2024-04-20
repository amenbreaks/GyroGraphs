package com.example.gyrographs

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class GyroSensorListener(
    private val onOrientationChange: (Float, Float, Float) -> Unit,
    private val databaseHelper: GyroDatabaseHelper
) : SensorEventListener {

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val (x, y, z) = event.values
            onOrientationChange(x, y, z)
            saveToDatabase(x, y, z)
        }
    }

    private fun saveToDatabase(x: Float, y: Float, z: Float) {
        databaseHelper.insertSensorData(x, y, z)
    }
}