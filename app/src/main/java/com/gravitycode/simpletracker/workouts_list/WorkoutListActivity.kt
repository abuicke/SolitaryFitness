package com.gravitycode.simpletracker.workouts_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepositoryImpl
import com.gravitycode.simpletracker.workouts_list.repository.dataStore
import com.gravitycode.simpletracker.workouts_list.util.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.EnumMap
import javax.inject.Inject

class WorkoutListActivity : ComponentActivity() {

    /**
     * TODO: Implement WorkoutHistoryRepositoryImpl with Proto DataStore
     * https://developer.android.com/topic/libraries/architecture/datastore
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
//        (applicationContext as SimpleTrackerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
//            WorkoutHistoryRepositoryImpl().getWorkoutHistory(this@WorkoutListActivity)

            val workouts = Workout.values()
            var i = 0

            dataStore.data.map { preferences ->
                val workoutName = workouts[i].toPrettyString()
                val count = preferences[intPreferencesKey(workouts[i++].toString())]
                /**
                 * I think I'm supposed to return something here? I have no idea what this is even doing
                 * */
                Log.i("read_work_history", "$workoutName: $count")
            }.collect { int ->
                Log.i("read_work_history", "$int")
            }
        }

        /**
         * How to use Rx with Model-ViewModel architecture for onClick
         */

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