package com.gravitycode.simpletracker.workout_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.workout_list.repository.WorkoutHistory
import com.gravitycode.simpletracker.workout_list.repository.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.repository.WorkoutHistoryRepositoryImpl
import com.gravitycode.simpletracker.workout_list.util.Workout
import kotlinx.coroutines.launch

class WorkoutListActivity : ComponentActivity() {

    /**
     * TODO: Write tests for [WorkoutHistoryRepositoryImpl] using the same code as below
     * TODO: Inject [DataStore] into [WorkoutHistoryRepositoryImpl] with Dagger
     * TODO: get, set, inc and dec functions on [WorkoutHistory] need to be synchronized
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (applicationContext as SimpleTrackerApp).appComponent.inject(this)
        val dataStore = (applicationContext as SimpleTrackerApp).preferencesDataStore
        val repo: WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)

        lifecycleScope.launch {
            repo.readWorkoutHistory().collect { workoutHistory ->
                Log.i("workout_history", "collect-1: $workoutHistory")
            }

            repo.writeWorkoutHistory(
                WorkoutHistory(
                    mapOf(
                        Workout.HANDSTAND_PRESS_UP to 4,
                        Workout.PRESS_UP to 8,
                        Workout.SIT_UP to 14,
                        Workout.SQUAT to 2,
                        Workout.SQUAT_THRUST to 4,
                        Workout.BURPEE to 25,
                        Workout.STAR_JUMP to 15,
                        Workout.STEP_UP to 0,
                    )
                )
            )

            repo.readWorkoutHistory().collect { workoutHistory ->
                Log.i("workout_history", "collect-2: $workoutHistory")
            }

            repo.writeWorkoutHistory(
                WorkoutHistory(
                    mapOf(
                        Workout.HANDSTAND_PRESS_UP to 12,
                        Workout.PRESS_UP to 32,
                        Workout.SIT_UP to 85,
                        Workout.SQUAT to 36,
                        Workout.SQUAT_THRUST to 100,
                        Workout.BURPEE to 200,
                        Workout.STAR_JUMP to 500,
                        Workout.STEP_UP to 50,
                    )
                )
            )

            repo.readWorkoutHistory().collect { workoutHistory ->
                Log.i("workout_history", "collect-3: $workoutHistory")
            }
        }

//        setContent {
//            SimpleTrackerTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    WorkoutList(onClick = {})
//                }
//            }
//        }
    }
}