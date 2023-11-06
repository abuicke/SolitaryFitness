package com.gravitycode.solitaryfitness.track_reps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gravitycode.solitaryfitness.app.SolitaryFitnessApp
import com.gravitycode.solitaryfitness.app.ui.SolitaryFitnessTheme
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsScreen
import com.gravitycode.solitaryfitness.track_reps.presentation.TrackRepsViewModel
import javax.inject.Inject

/**
 * "When you repeat yourself 3 times, then refactor..."
 *
 * TODO: Add UI tests to verify all the usual behavior I test manually.
 *
 * TODO: Sync data to Firebase (or somewhere) to make sure the record is never lost.
 *
 * TODO: Overflow:
 *          Reset
 *          Edit Reps - Adjust rep counts in each cell.
 *          Settings:
 *              1) Set custom values for rep buttons
 *              2) Boolean option: keep reps grid open until X is selected
 *
 *
 *
 *  TODO: I haven't been using UseCases. Where do they belong? In accessing the DataStore? Remember
 *      you can override the invoke operator `()` so they can be called like `XxUseCase(args...)`
 *
 * TODO: Run ProGuard on app for build
 *
 * TODO: There's a multiple DataStore instances exception on first install.
 *  When you run again after that it launches without issue.
 *  This doesn't seem to a problem in the release build or when it's downloaded from the PlayStore.
 *
 * TODO: Add Google Fit integration
 *
 * TODO: Implement number change animation. Like if the user clicks +10 you see the reps quickly
 *  count up from the current reps to +10.
 * */
class TrackRepsActivity : ComponentActivity() {

    private lateinit var trackRepsComponent: TrackRepsComponent
    @Inject lateinit var trackRepsViewModel: TrackRepsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as SolitaryFitnessApp).appComponent
        trackRepsComponent = appComponent.trackRepsComponent().create()
        trackRepsComponent.inject(this)

        setContent {
            SolitaryFitnessTheme {
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