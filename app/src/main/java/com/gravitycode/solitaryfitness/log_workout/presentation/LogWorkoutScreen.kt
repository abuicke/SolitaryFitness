/**
 * Composable functions should be idempotent, and free of side-effects:
 *
 * The rememberSaveable API behaves similarly to remember because it retains state across
 * recompositions, and also across activity or process recreation using the saved instance
 * state mechanism. For example, this happens, when the screen is rotated.
 *
 * [Restoring state in Compose](https://developer.android.com/jetpack/compose/state#restore-ui-state)
 * */
package com.gravitycode.solitaryfitness.log_workout.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.gravitycode.solitaryfitness.R
import com.gravitycode.solitaryfitness.app.AppEvent
import com.gravitycode.solitaryfitness.auth.User
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import com.gravitycode.solitaryfitness.util.error.debugError
import com.gravitycode.solitaryfitness.util.ui.ViewModel
import com.gravitycode.solitaryfitness.util.ui.compose.Grid
import com.gravitycode.solitaryfitness.util.ui.compose.OverflowMenu
import java.time.LocalDate

private const val TAG = "LogWorkoutScreen"

private enum class MenuItem(val string: String) {

    SIGN_IN("Sign In"),

    SIGN_OUT("Sign Out");

//    RESET_REPS("Reset Reps"),
//
//    EDIT_REPS("Edit Reps"),
//
//    SETTINGS("Settings");

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
fun LogWorkoutScreen(
    viewModel: ViewModel<LogWorkoutState, LogWorkoutEvent>,
    modifier: Modifier = Modifier
) {
    val workouts = Workout.values()
    val context = LocalContext.current

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val logWorkoutState = viewModel.state.value
        Log.d(TAG, "view model state updated: $logWorkoutState")

        TopBar(logWorkoutState.user) { item ->
            when (item) {
                MenuItem.SIGN_IN -> viewModel.onEvent(AppEvent.SignIn)
                MenuItem.SIGN_OUT -> viewModel.onEvent(AppEvent.SignOut)
//                MenuItem.RESET_REPS -> viewModel.onEvent(LogWorkoutEvent.Reset)
//                MenuItem.EDIT_REPS -> viewModel.onEvent(LogWorkoutEvent.Edit(LogWorkoutEvent.Edit.Mode.START))
//                MenuItem.SETTINGS -> Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show() //something to do with NavController
            }
        }
        TrackRepsGrid(
            modifier = Modifier.weight(1f),
            workouts = workouts,
            logWorkoutState = logWorkoutState,
            onEvent = viewModel::onEvent
        )
        // Setting startDate is only necessary on initialization, after that the date picker
        // updates itself and then also gets that date sent back to it from the event trigger,
        // but no recompose happens as the value is the same.
        // TODO: Make sure recompose doesn't happen twice when the date is changed, and test to see if the
        //  picker really is being set by the LogWorkoutState by setting the date to something earlier.
        //  What also happens if I set a date in the future on the LogWorkoutState? Should an exception be
        //  thrown in this instance if it's not already? I really don't like the behavior of how the date
        //  picker stays at the current date if you try to scroll to a future date. It should go to the max
        //  date allowed if you try to go past it.
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
private fun TopBar(
    user: User?,
    onMenuItemClicked: (MenuItem) -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(IntrinsicSize.Max).testTag("toolbar"),
        title = {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (user?.profilePicture != null) {
                    val imageLoaded = remember { mutableStateOf(false) }

                    AsyncImage(
                        modifier = Modifier
                            // Prevent the padding from taking up space before the image appears
                            .padding(end = if (imageLoaded.value) 12.dp else 0.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            // Don't show the image until it has finished loading
                            .fillMaxHeight(if (imageLoaded.value) 0.5f else 0.0f),
                        contentScale = ContentScale.Fit,
                        model = user.profilePicture,
                        placeholder = painterResource(R.drawable.placeholder_pfp),
                        contentDescription = "User profile picture",
                        onSuccess = { imageLoaded.value = true },
                        onError = { state ->
                            debugError("failed to load async image", state.result.throwable)
                        }
                    )
                }
                Text(
                    text = "Track Workouts",
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            val isUserSignedIn = user != null
            val menuItems = MenuItem.values().filter { menuItem ->
                when (menuItem) {
                    MenuItem.SIGN_IN -> !isUserSignedIn
                    MenuItem.SIGN_OUT -> isUserSignedIn
                    else -> true
                }
            }.map { it.string }

            OverflowMenu(
                modifier = Modifier.fillMaxHeight(),
                menuItems = menuItems
            ) { string ->
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
            modifier = Modifier.testTag(workout.string),
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