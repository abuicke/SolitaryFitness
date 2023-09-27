package com.gravitycode.simpletracker.workout_list

import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class WorkoutListModule {

    @Binds abstract fun provideWorkoutHistoryRepository(
        impl: WorkoutHistoryRepositoryImpl): WorkoutHistoryRepository
}