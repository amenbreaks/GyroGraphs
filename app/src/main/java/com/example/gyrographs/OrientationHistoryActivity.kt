package com.example.gyrographs

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gyrographs.ui.theme.GyroGraphsTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OrientationHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideGyroDatabase {
                GyroGraphsTheme {
                    Surface(color = Color.White) {
                        OrientationHistoryScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun OrientationHistoryScreen() {
    val orientationDataList = LocalGyroDatabase.current.getAllOrientationData()

    val xEntries = mutableListOf<Entry>()
    val yEntries = mutableListOf<Entry>()
    val zEntries = mutableListOf<Entry>()
    val timeValues = mutableListOf<Float>()

    orientationDataList.forEachIndexed { index, orientationData ->
        xEntries.add(Entry(index.toFloat(), orientationData.x))
        yEntries.add(Entry(index.toFloat(), orientationData.y))
        zEntries.add(Entry(index.toFloat(), orientationData.z))
        timeValues.add(orientationData.timestamp.toFloat())
    }

    LazyColumn {
        item {
            Text(text = "X Axis")
            ComposeChart(xEntries, timeValues)
        }
        item {
            Text(text = "Y Axis")
            ComposeChart(yEntries, timeValues)
        }
        item {
            Text(text = "Z Axis")
            ComposeChart(zEntries, timeValues)
        }
        item {
            ClearButton()
        }
    }
}

@Composable
fun ComposeChart(entries: List<Entry>, timeValues: List<Float>) {
    val dataSet = LineDataSet(entries, "Label")
    dataSet.color = android.graphics.Color.BLACK
    dataSet.setDrawCircleHole(false)
    val lineData = LineData(dataSet)
    val context = LocalContext.current
    val chart = LineChart(context)
    chart.setBackgroundColor(android.graphics.Color.WHITE)
    chart.setDrawGridBackground(false)

    chart.data = lineData

    val xAxis = chart.xAxis
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.granularity = 1f
    xAxis.valueFormatter = TimeAxisValueFormatter(timeValues)
    chart.invalidate()

    Box(
        modifier = Modifier
            .size(400.dp, 400.dp)
    ) {
        AndroidView(
            factory = { chart },
            modifier = Modifier.fillMaxSize()
        )
    }
}

class TimeAxisValueFormatter(private val timeValues: List<Float>) : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < timeValues.size) {
            timeValues[index].toString()
        } else {
            ""
        }
    }
}

@Composable
fun ClearButton() {
    val context = LocalContext.current

    Button(onClick = { clearDatabase(context) }) {
        Text(text = "Clear Database")
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun clearDatabase(context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        val dbHelper = GyroDatabaseHelper.getInstance(context)
        dbHelper.writableDatabase.delete(GyroDatabaseHelper.TABLE_NAME, null, null)
    }
}
