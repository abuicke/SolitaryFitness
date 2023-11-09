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

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.gravitycode.solitaryfitness.app.AppState
import com.gravitycode.solitaryfitness.track_reps.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.compose.Grid
import com.gravitycode.solitaryfitness.util.ui.compose.OverflowMenu
import java.time.LocalDate

private enum class MenuItem(val prettyString: String) {

    SIGN_IN("Sign In"),

    SIGN_OUT("Sign Out"),

    RESET_REPS("Reset Reps"),

    EDIT_REPS("Edit Reps"),

    SETTINGS("Settings");

    companion object {
        /**
         * Case insensitive way of converting from a string to [MenuItem]
         * */
        fun fromString(str: String): MenuItem? {
            return when (str.lowercase()) {
                "sign in" -> SIGN_IN
                "sign out" -> SIGN_OUT
                "reset reps" -> RESET_REPS
                "edit reps" -> EDIT_REPS
                "settings" -> SETTINGS
                else -> null
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun TrackRepsScreen() {
    TrackRepsScreen(
        modifier = Modifier.fillMaxSize(),
        appState = AppState(null),
        trackRepsState = TrackRepsState(LocalDate.now(), 0, 15, 30, 20, 9, 0, 45, 40),
        onEvent = { _ -> },
        onAppEvent = { _ -> }
    )
}

@Composable
fun TrackRepsScreen(
    modifier: Modifier = Modifier,
    appState: AppState,
    trackRepsState: TrackRepsState,
    onAppEvent: (AppEvent) -> Unit,
    onEvent: (TrackRepsEvent) -> Unit
) {
    val workouts = Workout.values()

    val context = LocalContext.current

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(appState.isUserSignedIn()) { item ->
            when (item) {
                MenuItem.SIGN_IN -> onAppEvent(AppEvent.SignIn)
                MenuItem.SIGN_OUT -> onAppEvent(AppEvent.SignOut)
                MenuItem.RESET_REPS -> Toast.makeText(context, "Reset Reps", Toast.LENGTH_SHORT).show()
                MenuItem.EDIT_REPS -> Toast.makeText(context, "Edit Reps", Toast.LENGTH_SHORT).show()
                MenuItem.SETTINGS -> Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
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
            val menuItems = MenuItem.values().filter { menuItem ->
                when (menuItem) {
                    MenuItem.SIGN_IN -> !isUserSignedIn
                    MenuItem.SIGN_OUT -> isUserSignedIn
                    else -> true
                }
            }.map { it.prettyString }

            OverflowMenu(menuItems) { string ->
                val menuItem: MenuItem? = MenuItem.fromString(string)
                if (menuItem != null) {
                    onMenuItemClicked(menuItem)
                } else {
                    debugError("Couldn't return MenuItem for string '$string'")
                }
            }
        }
    )
}

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
            workoutName = workout.prettyString,
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