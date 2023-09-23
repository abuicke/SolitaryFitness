package com.gravitycode.simpletracker.workouts_list

import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workouts_list.repository.WorkoutHistoryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class WorkoutListModule {

    @Binds abstract fun provideWorkoutHistoryRepository(
        impl: WorkoutHistoryRepositoryImpl): WorkoutHistoryRepository
}