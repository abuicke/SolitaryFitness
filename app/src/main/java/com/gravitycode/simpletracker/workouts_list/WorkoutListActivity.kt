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

        /**
         * TODO: Now [WorkoutHistoryRepositoryImpl.writeWorkoutHistory] is running twice on its own,
         * or not if put in between two calls to [WorkoutHistoryRepositoryImpl.readWorkoutHistory]!
         *
         * TODO: Sleep in-between calls
         * */
        lifecycleScope.launch {
//            repo.readWorkoutHistory()
            repo.writeWorkoutHistory()
//            repo.readWorkoutHistory()
//            repo.writeWorkoutHistory()
//            repo.readWorkoutHistory()
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