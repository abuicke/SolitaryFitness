package com.gravitycode.simpletracker.track_reps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gravitycode.simpletracker.app.SimpleTrackerApp
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsScreen
import com.gravitycode.simpletracker.track_reps.presentation.TrackRepsViewModel
import javax.inject.Inject

/**
 * TODO: Sync data to Firebase (or somewhere) and also support calendar functionality. Probably want
 *  to do the calendar functionality first as I'm going to need to decide how to structure the data.
 *  Multiple DataStore files? Or move over to ProtocolBuffers?
 *
 * TODO: Have a setting accessible from an overflow menu in the toolbar to set custom values for the
 *  add reps buttons. Technically the number of buttons available could also be customised but I
 *  don't know if there would be enough space on the screen.
 *
 *  TODO: I haven't been using UseCases. Where do they belong? In accessing the DataStore? Remember
 *      you can override the invoke operator `()` so they can be called like `XxUseCase(args...)`
 *
 * TODO: Write instrumented tests to ensure events work correctly. Test independently of UI.
 *
 * TODO: Test UI. Make sure reps values read correctly based on the add reps button pressed.
 *
 * TODO: Run ProGuard on app for build
 *
 * TODO: There's a multiple DataStore instances exception on first install.
 *  When you run again after that it launches without issue.
 *
 * TODO: Add Google Fit integration
 *
 * TODO: Need to sync data to cloud or somewhere else, Firebase? To make sure the record is never lost.
 *
 * TODO: Have UI options to switch between lifetime, day, month, week, calendar for different time periods.
 *
 * TODO: Should use string resources for Workout enum and in composables?
 * */
class TrackRepsActivity : ComponentActivity() {

    private lateinit var trackRepsComponent: TrackRepsComponent
    @Inject lateinit var trackRepsViewModelImpl: TrackRepsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as SimpleTrackerApp).appComponent
        trackRepsComponent = appComponent.trackRepsComponent().create()
        trackRepsComponent.inject(this)

        setContent {
            // TODO: If I remove this how does the [TrackRepsScreen] appear?
            SimpleTrackerTheme {
                // A surface container using the 'background' color from the theme
                // TODO: And this? What does this do?
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackRepsScreen(viewModel = trackRepsViewModelImpl)
                }
            }
        }
    }
}