/**
 * Composable functions should be idempotent, and free of side-effects:
 *
 * The rememberSaveable API behaves similarly to remember because it retains state across
 * recompositions, and also across activity or process recreation using the saved instance
 * state mechanism. For example, this happens, when the screen is rotated.
 *
 * [https://developer.android.com/jetpack/compose/state#restore-ui-state]
 * */
package com.gravitycode.solitaryfitness.log_workout.presentation

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
import androidx.compose.runtime.collectAsState
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
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.app.AppState
import com.gravitycode.solitaryfitness.log_workout.domain.WorkoutLog
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.debugError
import com.gravitycode.solitaryfitness.util.ui.ViewModel
import com.gravitycode.solitaryfitness.util.ui.compose.Grid
import com.gravitycode.solitaryfitness.util.ui.compose.OverflowMenu
import java.time.LocalDate

/**
 * TODO: Consider moving this out into it's won class and applying the same method of filters the values()
 *  function to be used in other screens. This enum should be all potential menu items which are then
 *  selected from on a per-screen and per-context basis.
 * */
private enum class MenuItem(val string: String) {

    SIGN_IN("Sign In"),

    SIGN_OUT("Sign Out"),

    RESET_REPS("Reset Reps"),

    EDIT_REPS("Edit Reps"),

    SETTINGS("Settings");

    companion object {

        fun fromString(str: String): MenuItem? {
            for (item in values()) {
                if (item.string == str) {
                    return item
                }
            }

            return null
        }
    }
}

//@Composable
//@Preview(showSystemUi = true)
//private fun TrackRepsScreen() {
//    TrackRepsScreen(
//        modifier = Modifier.fillMaxSize(),
//        appState = AppState(null),
//        logWorkoutState = LogWorkoutState(
//            LocalDate.now(),
//            WorkoutLog(0, 15, 30, 20, 9, 0, 45, 40)
//        ),
//        onEvent = { _ -> },
//        onAppEvent = { _ -> }
//    )
//}

@Composable
fun TrackRepsScreen(
    viewModel: ViewModel<LogWorkoutState, LogWorkoutEvent>,
    appController: AppController,
    modifier: Modifier = Modifier
) {
    val workouts = Workout.values()
    val context = LocalContext.current

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val appState = appController.appState.collectAsState()

        TopBar(appState.value.isUserSignedIn()) { item ->
            when (item) {
                MenuItem.SIGN_IN -> appController.requestSignIn()
                MenuItem.SIGN_OUT -> appController.requestSignOut()
                MenuItem.RESET_REPS -> viewModel.onEvent(LogWorkoutEvent.Reset)
                MenuItem.EDIT_REPS -> viewModel.onEvent(LogWorkoutEvent.Edit)
                MenuItem.SETTINGS -> Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show() //something to do with NavController
            }
        }
        TrackRepsGrid(
            modifier = Modifier.weight(1f),
            workouts = workouts,
            logWorkoutState = viewModel.state.value,
            onEvent = viewModel::onEvent
        )
        // Setting startDate is only necessary on initialization, after that the date picker
        // updates itself and then also gets that date sent back to it from the event trigger,
        // but no recompose happens as the value is the same.
        WheelDatePicker(
            startDate = viewModel.state.value.date,
            maxDate = LocalDate.now()
        ) { snappedDate ->
            viewModel.onEvent(LogWorkoutEvent.DateSelected(snappedDate))
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
            }.map { it.string }

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
    logWorkoutState: LogWorkoutState,
    onEvent: (LogWorkoutEvent) -> Unit
) {
    Grid(
        modifier = modifier,
        columns = 2,
        items = workouts.size,
        dividerColour = Color.Black
    ) { index ->
        val workout = workouts[index]

        WorkoutButton(
            workoutName = workout.string,
            currentReps = logWorkoutState.log[workout],
            onClickAddReps = { reps ->
                onEvent(LogWorkoutEvent.Increment(workout, reps))
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