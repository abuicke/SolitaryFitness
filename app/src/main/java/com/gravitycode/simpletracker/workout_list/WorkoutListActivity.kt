package com.gravitycode.simpletracker.workout_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepo
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepoImpl
import com.gravitycode.simpletracker.workout_list.domain.WorkoutListViewModel
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListScreen
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TODO: get, set, inc and dec functions on [WorkoutHistory] need to be synchronized
 * TODO: Implement onClick for [WorkoutListScreen]
 * */
class WorkoutListActivity : ComponentActivity() {

    private lateinit var workoutListComponent: WorkoutListComponent
    @Inject lateinit var workoutListViewModel: WorkoutListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (applicationContext as SimpleTrackerApp).appComponent
        workoutListComponent = appComponent.workoutListComponent().create()
        workoutListComponent.inject(this)

        setContent {
            SimpleTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutListScreen(workoutListViewModel)
                }
            }
        }
    }
}