package com.gravitycode.simpletracker.workouts_list.ui

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
import com.gravitycode.simpletracker.workouts_list.Workouts
import com.gravitycode.simpletracker.util.Numbers

@Composable
fun WorkoutsList(modifier: Modifier = Modifier, onClick: () -> Unit) {
    LazyList(modifier, Workouts.DEFAULT_WORKOUTS, onClick)
}

@Composable
private fun <E> LazyList(
    modifier: Modifier = Modifier,
    listItems: List<E>,
    onClick: (() -> Unit)
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
                        onClick()
                    }
            ) {
                Row() {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = listItem.toString(),
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = Numbers.ZERO.string,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}