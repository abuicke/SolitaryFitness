package com.gravitycode.simpletracker.track_reps.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.gravitycode.simpletracker.track_reps.util.Workout
import java.time.LocalDate

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
@Preview(showSystemUi = true)
private fun TrackRepsScreen() {
    TrackRepsScreen(
        Modifier.fillMaxSize(),
        trackRepsState = TrackRepsState(
            LocalDate.now(),
            273750000,
            547500000,
            54750000,
            27375000,
            5475000,
            136875000,
            246375000,
            438000000
        ),
        onEvent = { _ -> }
    )
}

/**
 * TODO: Need to learn more about [NavController] and if it's even a good solution.
 * */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TrackRepsScreen(
    modifier: Modifier = Modifier,
//    navController: NavController,
    trackRepsState: TrackRepsState,
    onEvent: (TrackRepsEvent) -> Unit
) {
    val workouts = Workout.values()

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.track_reps_topbar_text),
                    color = MaterialTheme.colorScheme.background
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.overflow_icon_content_description),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        )
        /**
         * TODO: Apply Grid() abstraction here too
         * */
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
                        reps = trackRepsState[firstWorkout],
                        onClickReps = { workout, reps ->
                            onEvent(TrackRepsEvent.Increment(workout, reps))
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
                        reps = trackRepsState[secondWorkout],
                        onClickReps = { workout, reps ->
                            onEvent(TrackRepsEvent.Increment(workout, reps))
                        }
                    )
                }
                Divider(color = Color.Black)
            }
        }
        // Setting startDate is only necessary on initialization, after that the date picker
        // updates itself and then also gets that date sent back to it from the event trigger,
        // but no recompose happens as the value is the same.
        WheelDatePicker(startDate = trackRepsState.date) { snappedDate ->
            onEvent(TrackRepsEvent.DateSelected(snappedDate))
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
                        Text(text = "1")
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
                        Text(text = "5")
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
                        Text(text = "10")
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
                        Text(text = "X")
                    }
                }
            }
        }
    }
}