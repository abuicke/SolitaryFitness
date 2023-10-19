package com.gravitycode.simpletracker.workout_list.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.gravitycode.simpletracker.workout_list.presentation.preview.PreviewWorkoutListViewModel
import com.gravitycode.simpletracker.workout_list.util.Workout

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
fun WorkoutListScreen() {
    WorkoutListScreen(viewModel = PreviewWorkoutListViewModel(allReps = 10000))
}

/**
 * TODO: Need to learn more about [NavController] and if it's even a good solution.
 * TODO: Need to account for when number becomes very long. Push title more and more to the left?
 * TODO: Implement number change animation. Like if the user clicks +10 you see the reps quickly
 *  count up from the current reps to +10.
 * */
@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
//    navController: NavController,
    viewModel: WorkoutListViewModel
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
                            viewModel.onEvent(WorkoutListEvent.Increment(workout, reps))
                        }
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    WorkoutButton(
                        modifier = Modifier.weight(1f),
                        workout = secondWorkout,
                        reps = listState[secondWorkout],
                        onClickReps = { workout, reps ->
                            viewModel.onEvent(WorkoutListEvent.Increment(workout, reps))
                        }
                    )
                }
                Divider()
            }
        }

        WheelDatePicker { snappedDate ->
            viewModel.onEvent(WorkoutListEvent.DateSelected(snappedDate))
        }
    }
}

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
            /**
             * TODO: Grid should show 1, 5, 10 and X
             * */
        }
    }
}