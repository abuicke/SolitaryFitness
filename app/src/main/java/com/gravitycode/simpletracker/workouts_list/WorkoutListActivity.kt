package com.gravitycode.simpletracker.workouts_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistory
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepositoryImpl
import kotlinx.coroutines.launch

class WorkoutListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = (applicationContext as SimpleTrackerApp).preferencesDataStore

        lifecycleScope.launch {
            val repo: WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl(dataStore)
            repo.writeWorkoutHistory()
            repo.readWorkoutHistory()
            repo.writeWorkoutHistory()
            repo.readWorkoutHistory()
        }
    }
}