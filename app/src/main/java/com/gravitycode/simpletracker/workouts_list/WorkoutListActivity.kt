package com.gravitycode.simpletracker.workouts_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.workouts_list.ui.WorkoutList

class WorkoutListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * How to use Rx with Model-ViewModel architecture for onClick
         */

        setContent {
            SimpleTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutList(onClick = {})
                }
            }
        }
    }
}