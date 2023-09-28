package com.gravitycode.simpletracker.workout_list.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gravitycode.simpletracker.util.Number
import com.gravitycode.simpletracker.workout_list.domain.WorkoutListViewModel
import com.gravitycode.simpletracker.workout_list.util.Workout

/**
 * TODO: Need to learn more about [NavController] and if it's even a good solution.
 * TODO: Need to prevent long strings in list from pushing count out of sight,
 * replace with ellipse if text takes up too much space on smaller screen sizes.
 * */
@Composable
fun WorkoutListScreen(
//    navController: NavController,
    viewModel: WorkoutListViewModel
) {
    LazyList(
        listItems = Workout.values(),
        onClick = { workout ->
            viewModel.doSomething(workout)
        },
        onRender = { workout ->
            workout.toPrettyString()
        }
    )
}

@Composable
private fun <E> LazyList(
    modifier: Modifier = Modifier,
    listItems: Array<E>,
    onClick: (E) -> Unit = {},
    onRender: (E) -> String = { e -> e.toString() }
) = LazyList(modifier, listItems.toList(), onClick, onRender)

@Composable
private fun <E> LazyList(
    modifier: Modifier = Modifier,
    listItems: List<E>,
    onClick: ((E) -> Unit) = {},
    onRender: (E) -> String = { e -> e.toString() }
) {
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listItems) { listItem ->
            Card(
                modifier = Modifier
                    .padding(12.dp, 6.dp, 12.dp, 6.dp)
                    .fillMaxSize()
                    .clickable {
                        onClick(listItem)
                    }
            ) {
                Row() {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = onRender(listItem),
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = Number.ZERO.string,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}