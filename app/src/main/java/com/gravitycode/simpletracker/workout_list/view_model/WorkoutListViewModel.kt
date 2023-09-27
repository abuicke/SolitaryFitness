package com.gravitycode.simpletracker.workout_list.view_model

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.subjects.BehaviorSubject

class WorkoutListViewModel constructor() : ViewModel() {

    val observable: BehaviorSubject<WorkoutListState> = BehaviorSubject.create()
}
