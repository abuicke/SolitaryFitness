package com.gravitycode.simpletracker.workout_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.util.Preconditions
import com.google.common.collect.ImmutableMap
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListViewModel
import com.gravitycode.simpletracker.workout_list.presentation.WorkoutListScreen
import javax.inject.Inject

/**
 * Use data classes where applicable
 *
 * TODO: Write instrumented tests to ensure events work correctly. Test independently of UI.
 *
 * TODO: If I keep Guava, replace exception throws with [Preconditions.checkArgument]
 *
 * TODO: URLs work in Kdoc by using an anchor tag with a href attribute, @see [ImmutableMap] docs
 *
 * TODO: Run ProGuard on app for build
 *
 * TODO: Packages should be restructured. Look into what presentation, data and domain should contain.
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
                    WorkoutListScreen(viewModel = workoutListViewModel)
                }
            }
        }
    }
}