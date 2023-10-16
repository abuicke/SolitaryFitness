package com.gravitycode.simpletracker.workout_list.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gravitycode.simpletracker.workout_list.presentation.preview.PreviewWorkoutListViewModel
import com.gravitycode.simpletracker.workout_list.util.Workout

/**
 * Composable functions should be idempotent, and free of side-effects:
 *
 *      1) The function behaves the same way when called multiple times with the same argument,
 *      and it does not use other values such as global variables or calls to random().
 *
 *      2) The function describes the UI without any side-effects,
 *      such as modifying properties or global variables.
 *
 * [https://developer.android.com/jetpack/compose/mental-model#simple-example]
 *
 *
 *
 *
 * Never depend on side-effects from executing composable functions, since a function's
 * recomposition may be skipped. If you do, users may experience strange and unpredictable
 * behavior in your app. A side-effect is any change that is visible to the rest of your app.
 * For example, these actions are all dangerous side-effects:
 *
 *      1) Writing to a property of a shared object
 *      2) Updating an observable in ViewModel
 *      3) Updating shared preferences
 *
 * If you need to do expensive operations, such as reading from shared preferences, do it in
 * a background coroutine and pass the value result to the composable function as a parameter.
 *
 * [https://developer.android.com/jetpack/compose/mental-model#recomposition]
 *
 *
 *
 *
 * To ensure your application behaves correctly, all composable functions
 * should have no side-effects. Instead, trigger side-effects from
 * callbacks such as onClick that always execute on the UI thread.
 *
 * [https://developer.android.com/jetpack/compose/mental-model#parallel]
 *
 *
 *
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
    WorkoutListScreen(viewModel = PreviewWorkoutListViewModel(10000))
}

/**
 * TODO: Need to learn more about [NavController] and if it's even a good solution.
 * TODO: Once the reps get past 1000, should start using K notation, like 1.2K, 50.8K etc.
 * */
@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
//    navController: NavController,
    viewModel: WorkoutListViewModel
) {
    val listState = viewModel.state.value
    val workouts = remember { Workout.values() }

    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(workouts) { i, workout ->

            val isShowingRepButtons = remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .padding(12.dp, 6.dp, 12.dp, 6.dp)
                    .clickable {
//                        if (isShowingRepButtons.value) {
//                            isShowingRepButtons.value = false
//                            /**
//                             * TODO: How do I know which one was clicked?
//                             * */
//                            viewModel.onEvent(WorkoutListEvent.Increment(workouts[i], 1))
//                        } else {
//                            isShowingRepButtons.value = true
//                        }
                    }
            ) {
                Box() {
                    TitleAndCount(
                        workout = workout,
                        listState = listState
                    )
                    AddRepsButtons()
                }
            }
        }
    }
}

/**
 * TODO: Is it correct to pass [androidx.compose.runtime.State]
 *  and remembered object into a nested composable like this?
 * */
@Composable
fun TitleAndCount(
    modifier: Modifier = Modifier,
    workout: Workout,
    listState: WorkoutListState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .weight(0.7f),
            // TODO: Should just pass in the result of `workout.toPrettyString()` here
            text = workout.toPrettyString(),
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(0.3f),
            // TODO: Should just pass in the result of `listState[workout].toString()` here
            text = listState[workout].toString(),
            textAlign = TextAlign.Right,
            fontSize = 24.sp
        )
    }
}

/**
 * TODO: Should move sizing into [Box] and set [Modifier.fillMaxSize] or [Modifier.fillMaxHeight] on both
 * */
@Composable
fun AddRepsButtons(modifier: Modifier = Modifier) {
    Row(modifier) {
        Text("+1")
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text("+5")
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text("+10")
    }
}