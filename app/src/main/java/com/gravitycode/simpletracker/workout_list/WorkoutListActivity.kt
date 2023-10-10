package com.gravitycode.simpletracker.workout_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListScreen
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListViewModel
import javax.inject.Inject

/**
 * TODO: Write instrumented tests to ensure events work correctly. Test independently of UI.
 *
 * TODO: Run ProGuard on app for build
 *
 * TODO: There's a multiple DataStore instances exception on first install.
 *  When you run again after that it launches without issue.
 *
 * TODO: Add Google Fit integration
 *
 * TODO: Write tests for as much app functionality as can be managed.
 *
 * TODO: Need to sync data to cloud or somewhere else, Firebase? To make sure the record is never lost.
 *
 * TODO: Have UI options to switch between lifetime, day, month, week, calendar for different time periods.
 * */
class WorkoutListActivity : ComponentActivity() {

    private lateinit var workoutListComponent: WorkoutListComponent
    @Inject lateinit var workoutListViewModelImpl: WorkoutListViewModel

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
                    WorkoutListScreen(viewModel = workoutListViewModelImpl)
                }
            }
        }
    }
}