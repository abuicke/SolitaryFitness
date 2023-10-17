package com.gravitycode.simpletracker.workout_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
 * The rememberSaveable API behaves similarly to remember because it retains state across
 * recompositions, and also across activity or process recreation using the saved instance
 * state mechanism. For example, this happens, when the screen is rotated.
 *
 * [https://developer.android.com/jetpack/compose/state#restore-ui-state]
 * */

/*
* Colours
* */
private val TITLE_AND_COUNT_BTN_COLOUR = Color(140, 177, 189)
private val TITLE_AND_COUNT_TXT_COLOUR = Color(77, 77, 77)
private val ADD_REPS_BTN_COLOUR = TITLE_AND_COUNT_BTN_COLOUR
private val ADD_REPS_BTN_COLOUR_PRESSED = Color(125, 163, 176)
private val ADD_REPS_BTN_TXT_COLOUR = TITLE_AND_COUNT_TXT_COLOUR

/*
* Text sizes and fonts
* */
private val TITLE_AND_COUNT_TXT_SIZE = 24.sp
private val ADD_REPS_BTN_TXT_SIZE = TITLE_AND_COUNT_TXT_SIZE

@Composable
@Preview(showSystemUi = true)//, widthDp = 250)
fun WorkoutListScreen() {
    WorkoutListScreen(viewModel = PreviewWorkoutListViewModel(10000))
}

/**
 * TODO: Need to learn more about [NavController] and if it's even a good solution.
 * TODO: Need to account for when number becomes very long. Push title more and more to the left?
 * */
@Composable
fun WorkoutListScreen(
    modifier: Modifier = Modifier,
//    navController: NavController,
    viewModel: WorkoutListViewModel
) {
    val listState = viewModel.state.value

    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val workouts = Workout.values()
        items(workouts) { workout: Workout ->

            val isShowingRepButtons = remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .padding(12.dp, 6.dp, 12.dp, 6.dp)
                    .clickable {
                        if (!isShowingRepButtons.value) {
                            isShowingRepButtons.value = true
                        }
                    }
            ) {
                // TODO: Min and Max do the same thing.
                //  Don't know what the difference is.
                Box(modifier.height(IntrinsicSize.Min)) {
                    TitleAndCount(
                        title = workout.toPrettyString(),
                        count = listState[workout]
                    )
                    if (isShowingRepButtons.value) {
                        AddRepsButtonRow(Modifier.fillMaxSize()) { reps: Int? ->
                            isShowingRepButtons.value = false
                            if (reps != null) {
                                viewModel.onEvent(WorkoutListEvent.Increment(workout, reps))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * TODO: Use thinner font for title [https://developer.android.com/jetpack/compose/text/fonts]
 * */
@Composable
fun TitleAndCount(
    modifier: Modifier = Modifier,
    title: String,
    count: Int
) {
    Row(
        modifier = modifier.background(TITLE_AND_COUNT_BTN_COLOUR),
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
            text = title,
            color = TITLE_AND_COUNT_TXT_COLOUR,
            fontSize = TITLE_AND_COUNT_TXT_SIZE
        )
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(0.3f),
            text = count.toString(),
            color = TITLE_AND_COUNT_TXT_COLOUR,
            textAlign = TextAlign.Right,
            fontSize = TITLE_AND_COUNT_TXT_SIZE
        )
    }
}

/**
 * TODO: Buttons should change to a lighter colour while pressed.
 * TODO: Add 'X' to exit from adding reps
 * */
@Composable
fun AddRepsButtonRow(modifier: Modifier = Modifier, onClickReps: (Int?) -> Unit) {
    Row(
        modifier.background(ADD_REPS_BTN_COLOUR),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AddRepsButton(Modifier.weight(1f), 1, onClickReps)
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        AddRepsButton(Modifier.weight(1f), 5, onClickReps)
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        AddRepsButton(Modifier.weight(1f), 10, onClickReps)
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = { onClickReps(null) }
        ) {
            Text(
                text = "X",
                color = ADD_REPS_BTN_TXT_COLOUR,
                textAlign = TextAlign.Center,
                fontSize = ADD_REPS_BTN_TXT_SIZE
            )
        }
    }
}

@Composable
fun AddRepsButton(modifier: Modifier, reps: Int, onClickReps: (Int) -> Unit) {

    /**
     * TODO: Pressed colour never gets seen
     * */
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val bgColour = if (isPressed) ADD_REPS_BTN_COLOUR_PRESSED else ADD_REPS_BTN_COLOUR

    TextButton(
        modifier = modifier.background(bgColour),
        onClick = { onClickReps(reps) },
        interactionSource = interactionSource
    ) {
        Text(
            text = "+$reps",
            color = ADD_REPS_BTN_TXT_COLOUR,
            textAlign = TextAlign.Center,
            fontSize = ADD_REPS_BTN_TXT_SIZE
        )
    }
}