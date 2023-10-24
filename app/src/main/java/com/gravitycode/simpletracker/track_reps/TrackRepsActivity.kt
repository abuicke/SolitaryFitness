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
 * "When you repeat yourself 3 times, then refactor..."
 *
 * TODO: Sync data to Firebase (or somewhere) to make sure the record is never lost.
 *
 * TODO: Have a setting accessible from an overflow menu in the toolbar to set custom values for the
 *  add reps buttons.
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
 * TODO: Should use string resources for Workout enum and in composables?
 *
 * TODO: [TrackRepsScreen] crashes and doesn't display correctly on rotation.
 *
 * TODO: Implement number change animation. Like if the user clicks +10 you see the reps quickly
 *  count up from the current reps to +10.
 *
 * TODO: Settings:
 *      1) Set custom values for rep buttons
 *      2) Boolean option: close rep buttons grid after adding reps true/false
 *
 * TODO: Set app icon to flex emoji
 * */
class TrackRepsActivity : ComponentActivity() {

    private lateinit var trackRepsComponent: TrackRepsComponent
    @Inject lateinit var trackRepsViewModel: TrackRepsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as SimpleTrackerApp).appComponent
        trackRepsComponent = appComponent.trackRepsComponent().create()
        trackRepsComponent.inject(this)

        setContent {
            SimpleTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackRepsScreen(
                        trackRepsState = trackRepsViewModel.state.value,
                        onEvent = trackRepsViewModel::onEvent
                    )
                }
            }
        }
    }
}