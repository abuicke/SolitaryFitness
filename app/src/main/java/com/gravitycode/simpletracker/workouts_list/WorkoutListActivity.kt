package com.gravitycode.simpletracker.workouts_list

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.util.withDefault
import com.gravitycode.simpletracker.workouts_list.util.Workout
import timber.log.Timber
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

//        val workoutReps = EnumMap<Workout, Int>(Workout::class.java)

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