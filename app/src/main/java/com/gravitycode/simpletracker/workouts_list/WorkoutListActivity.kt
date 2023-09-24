package com.gravitycode.simpletracker.workouts_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistory
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepository
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
            /**
             * TODO: Functions not working when called one after the other. Why?
             * https://stackoverflow.com/questions/65171675/kotlin-coroutine-doesnt-execute-multiple-suspend-functions
             * https://stackoverflow.com/questions/60770793/will-these-suspend-functions-inside-in-a-parent-suspend-function-be-run-in-seque
             * */
            val workoutHistoryRepository: WorkoutHistoryRepository = WorkoutHistoryRepositoryImpl()
//            workoutHistoryRepository.readWorkoutHistory(this@WorkoutListActivity)
            workoutHistoryRepository.writeWorkoutHistory(this@WorkoutListActivity, WorkoutHistory())
//            workoutHistoryRepository.readWorkoutHistory(this@WorkoutListActivity)
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