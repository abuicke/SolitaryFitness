package com.gravitycode.simpletracker.workouts_list.view_model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class WorkoutListViewModel @Inject constructor(): ViewModel() {

    val observable: BehaviorSubject<WorkoutListState> = BehaviorSubject.create()
}
