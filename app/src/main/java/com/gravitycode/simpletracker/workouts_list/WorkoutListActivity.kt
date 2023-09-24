package com.gravitycode.simpletracker.workouts_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepositoryImpl
import com.gravitycode.simpletracker.workouts_list.util.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

        GlobalScope.launch(Dispatchers.IO) {
            WorkoutHistoryRepositoryImpl().getWorkoutHistory(this@WorkoutListActivity)
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