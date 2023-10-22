package com.gravitycode.simpletracker.track_reps.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.gravitycode.simpletracker.R
import com.gravitycode.simpletracker.app.ui.SimpleTrackerTheme
import com.gravitycode.simpletracker.track_reps.presentation.preview.PreviewTrackRepsViewModel
import com.gravitycode.simpletracker.track_reps.util.Workout

/**
 * Composable functions should be idempotent, and free of side-effects:
 *
 * The rememberSaveable API behaves similarly to remember because it retains state across
 * recompositions, and also across activity or process recreation using the saved instance
 * state mechanism. For example, this happens, when the screen is rotated.
 *
 * [https://developer.android.com/jetpack/compose/state#restore-ui-state]
 * */
@Composable
@Preview(showSystemUi = true)//, widthDp = 250)
fun TrackRepsScreen() {
    /**
     * TODO: The [SimpleTrackerTheme] and [androidx.compose.material3.Surface] should be wrapped
     *  around the preview the same way they are in [com.gravitycode.simpletracker.track_reps.TrackRepsActivity]
     *  once I find out what those functions do.
     * */
    TrackRepsScreen(viewModel = PreviewTrackRepsViewModel(allReps = 10000))
}

/**
 * TODO: Need to learn more about [NavController] and if it's even a good solution.
 * TODO: Need to account for when number becomes very long. Push title more and more to the left?
 * TODO: Implement number change animation. Like if the user clicks +10 you see the reps quickly
 *  count up from the current reps to +10.
 * TODO: The date on the date picker should be set from the view model. The view shouldn't take it
 *  for granted that the view model initial date is set to today.
 * */
@Composable
fun TrackRepsScreen(
    modifier: Modifier = Modifier,
//    navController: NavController,
    viewModel: TrackRepsViewModel
) {
    val listState = viewModel.state.value
    val workouts = Workout.values()

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier.weight(1f)) {
            for (i in workouts.indices step 2) {
                val firstWorkout = workouts[i]
                val secondWorkout = workouts[i + 1]

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkoutButton(
                        modifier = Modifier.weight(1f),
                        workout = firstWorkout,
                        reps = listState[firstWorkout],
                        onClickReps = { workout, reps ->
                            viewModel.onEvent(TrackRepsEvent.Increment(workout, reps))
                        }
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = Color.Black
                    )
                    WorkoutButton(
                        modifier = Modifier.weight(1f),
                        workout = secondWorkout,
                        reps = listState[secondWorkout],
                        onClickReps = { workout, reps ->
                            viewModel.onEvent(TrackRepsEvent.Increment(workout, reps))
                        }
                    )
                }
                Divider(
                    color = Color.Black
                )
            }
        }
        WheelDatePicker { snappedDate ->
            viewModel.onEvent(TrackRepsEvent.DateSelected(snappedDate))
        }
    }
}

/**
 * TODO: Extract the grid into a general purpose `Grid()` composable.
 *  Too tired to figure out how to do it right now.
 *  Could find useful examples here: [https://github.com/nesyou01/LazyStaggeredGrid],
 *  [https://developer.android.com/jetpack/compose/lists]
 * */
@Composable
fun WorkoutButton(
    modifier: Modifier,
    workout: Workout,
    reps: Int,
    onClickReps: (Workout, Int) -> Unit
) {
    Box(modifier) {
        val isShowingAddRepsGrid = remember { mutableStateOf(false) }

        if (!isShowingAddRepsGrid.value) {
            TextButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    if (!isShowingAddRepsGrid.value) {
                        isShowingAddRepsGrid.value = true
                    }
                }
            ) {
                Text(
                    text = reps.toString(),
                    fontSize = 30.sp
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                text = workout.toPrettyString(),
                fontSize = 12.sp
            )
        } else {
            Column {
                Row(
                    Modifier.weight(1f),
                ) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        onClick = {
                            onClickReps(workout, 1)
                            isShowingAddRepsGrid.value = false
                        }
                    ) {
                        Text(
                            text = "1"
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        onClick = {
                            onClickReps(workout, 5)
                            isShowingAddRepsGrid.value = false
                        }
                    ) {
                        Text(
                            text = "5"
                        )
                    }
                }
                Divider()
                Row(Modifier.weight(1f)) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        onClick = {
                            onClickReps(workout, 10)
                            isShowingAddRepsGrid.value = false
                        }
                    ) {
                        Text(
                            text = "10"
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        onClick = { isShowingAddRepsGrid.value = false }
                    ) {
                        Text(
                            text = "X"
                        )
                    }
                }
            }
        }
    }
}