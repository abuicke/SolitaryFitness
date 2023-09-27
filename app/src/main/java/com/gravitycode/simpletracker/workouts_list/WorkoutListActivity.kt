package com.gravitycode.simpletracker.workouts_list

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
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistory
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepositoryImpl
import kotlinx.coroutines.launch

class WorkoutListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (applicationContext as SimpleTrackerApp).appComponent.inject(this)
        val dataStore = (applicationContext as SimpleTrackerApp).preferencesDataStore
        val repo: WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)

        lifecycleScope.launch {
            try {
                val workoutHistory1 = repo.readWorkoutHistory()
                Log.i("workout_history", "collect-1: $workoutHistory1")

                repo.writeWorkoutHistory()

                val workoutHistory2 = repo.readWorkoutHistory()
                Log.i("workout_history", "collect-2: $workoutHistory2")

                repo.writeWorkoutHistory()

                val workoutHistory3 = repo.readWorkoutHistory()
                Log.i("workout_history", "collect-3: $workoutHistory3")
            } catch (t: Throwable) {
                Log.e("workout_history", "error", t)
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