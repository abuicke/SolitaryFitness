package com.gravitycode.simpletracker.workout_list

import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepository
import com.gravitycode.simpletracker.workout_list.data.WorkoutHistoryRepositoryImpl
import com.gravitycode.simpletracker.workout_list.domain.WorkoutListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * TODO: "You should only declare modules once in a component." is the advice given in [https://developer.android.com/training/dependency-injection/dagger-android]
 * but I don't understand why that is at all. I should make a component for each feature? Or put everything for all
 * features in one module? I'm confused by this advice.
 *
 * TODO: Need to use Android scopes, e.g. @ActivityScope
 * */
@Module
abstract class WorkoutListModule {

    @Binds abstract fun provideWorkoutHistoryRepository(
        impl: WorkoutHistoryRepositoryImpl
    ): WorkoutHistoryRepository

    @Module
    object StaticProvides {
        @JvmStatic @Provides fun provideWorkoutListViewModel() = WorkoutListViewModel()
    }
}