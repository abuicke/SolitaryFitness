/**
 * Composable functions should be idempotent, and free of side-effects:
 *
 * The rememberSaveable API behaves similarly to remember because it retains state across
 * recompositions, and also across activity or process recreation using the saved instance
 * state mechanism. For example, this happens, when the screen is rotated.
 *
 * [https://developer.android.com/jetpack/compose/state#restore-ui-state]
 * */
package com.gravitycode.solitaryfitness.track_reps.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.gravitycode.solitaryfitness.R
import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.track_reps.TrackRepsActivity
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import com.gravitycode.solitaryfitness.util.ui.compose.Grid
import com.gravitycode.solitaryfitness.util.ui.compose.OverflowMenu
import java.time.LocalDate

private enum class MenuItem {
    SIGN_IN, SIGN_OUT, RESET_REPS, EDIT_REPS, SETTINGS
}

@Composable
@Preview(showSystemUi = true)
private fun TrackRepsScreen() {
    TrackRepsScreen(
        modifier = Modifier.fillMaxSize(),
        isUserSignedIn = true,
        trackRepsState = TrackRepsState(LocalDate.now(), 0, 15, 30, 20, 9, 0, 45, 40),
        onEvent = { _ -> }
    )
}

@Composable
fun TrackRepsScreen(
    modifier: Modifier = Modifier,
    isUserSignedIn: Boolean,
    trackRepsState: TrackRepsState,
    onEvent: (AppEvent<out TrackRepsEvent>) -> Unit
) {
    val workouts = Workout.values()

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(isUserSignedIn) { item ->
            when (item) {
                MenuItem.SIGN_IN -> onEvent(AppEvent.SignIn)
                MenuItem.SIGN_OUT -> onEvent(AppEvent.SignOut)
                MenuItem.RESET_REPS -> TODO()
                MenuItem.EDIT_REPS -> TODO()
                MenuItem.SETTINGS -> TODO()
            }
        }
        TrackRepsGrid(
            modifier = Modifier.weight(1f),
            workouts = workouts,
            trackRepsState = trackRepsState,
            onEvent = onEvent
        )
        // Setting startDate is only necessary on initialization, after that the date picker
        // updates itself and then also gets that date sent back to it from the event trigger,
        // but no recompose happens as the value is the same.
        WheelDatePicker(startDate = trackRepsState.date) { snappedDate ->
            onEvent(TrackRepsEvent.DateSelected(snappedDate))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(isUserSignedIn: Boolean, onMenuItemClicked: (MenuItem) -> Unit) {
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
            OverflowMenu {
                if (isUserSignedIn) {
                    DropdownMenuItem({ Text("Sign Out") }, { onMenuItemClicked(MenuItem.SIGN_OUT) })
                } else {
                    DropdownMenuItem({ Text("Sign In") }, { onMenuItemClicked(MenuItem.SIGN_IN) })
                }
                DropdownMenuItem({ Text("Reset Reps") }, { onMenuItemClicked(MenuItem.RESET_REPS) })
                DropdownMenuItem({ Text("Edit Reps") }, { onMenuItemClicked(MenuItem.EDIT_REPS) })
                DropdownMenuItem({ Text("Settings") }, { onMenuItemClicked(MenuItem.SETTINGS) })
            }
        }
    )
}

/**
 * TODO: Grid should size dynamically based on the array past in
 * */
@Composable
private fun TrackRepsGrid(
    modifier: Modifier = Modifier,
    workouts: Array<Workout>,
    trackRepsState: TrackRepsState,
    onEvent: (TrackRepsEvent) -> Unit
) {
    Grid(
        modifier = modifier,
        columns = 2,
        items = workouts.size,
        dividerColour = Color.Black
    ) { index ->
        val workout = workouts[index]

        WorkoutButton(
            workoutName = workout.toPrettyString(),
            currentReps = trackRepsState[workout],
            onClickAddReps = { reps ->
                onEvent(TrackRepsEvent.Increment(workout, reps))
            }
        )
    }
}

@Composable
private fun WorkoutButton(
    modifier: Modifier = Modifier,
    workoutName: String,
    currentReps: Int,
    onClickAddReps: (Int) -> Unit
) {
    val isShowingAddRepsGrid = remember { mutableStateOf(false) }

    if (!isShowingAddRepsGrid.value) {
        RepsCount(
            modifier = modifier,
            workoutName = workoutName,
            reps = currentReps,
            onClick = {
                if (!isShowingAddRepsGrid.value) {
                    isShowingAddRepsGrid.value = true
                }
            }
        )
    } else {
        AddRepsGrid(modifier) { reps: Int? ->
            if (reps != null) onClickAddReps(reps)
            isShowingAddRepsGrid.value = false
        }
    }
}

@Composable
private fun RepsCount(
    modifier: Modifier = Modifier,
    workoutName: String,
    reps: Int,
    onClick: () -> Unit,
) {
    Box(modifier) {
        TextButton(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick
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
            text = workoutName,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun AddRepsGrid(
    modifier: Modifier,
    onClickAddReps: (Int?) -> Unit
) {
    Grid(
        modifier = modifier,
        columns = 2,
        items = 4,
    ) { cell ->
        TextButton(
            modifier = Modifier.fillMaxSize(),
            onClick = {
                when (cell) {
                    0 -> onClickAddReps(1)
                    1 -> onClickAddReps(5)
                    2 -> onClickAddReps(10)
                    3 -> onClickAddReps(null)
                    else -> error("invalid cell")
                }
            }
        ) {
            Text(
                text = when (cell) {
                    0 -> "1"
                    1 -> "5"
                    2 -> "10"
                    3 -> "X"
                    else -> error("invalid cell")
                }
            )
        }
    }
}