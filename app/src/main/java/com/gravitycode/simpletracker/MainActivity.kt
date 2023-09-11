package com.gravitycode.simpletracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gravitycode.simpletracker.ui.theme.SimpleTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WorkoutsList()
                }
            }
        }
    }
}

@Composable
fun WorkoutsList() {
 LazyList(listItems = Workouts.DEFAULT_WORKOUTS)
}

@Composable
fun <E> LazyList(listItems: List<E>, modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(listItems) { listItem ->
            Text(text = listItem.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutsListPreview() {
    WorkoutsList()
}