package com.example.gyrographs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gyrographs.ui.theme.GyroGraphsTheme

class OrientationHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideGyroDatabase {
                GyroGraphsTheme {
                    Surface {
                        // Call a composable function to display the orientation history
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
    
    Text(text = "yooooooooo")

    // Use a charting library to plot the orientation history
    // Example: LineChart for X, Y, Z axes
}
