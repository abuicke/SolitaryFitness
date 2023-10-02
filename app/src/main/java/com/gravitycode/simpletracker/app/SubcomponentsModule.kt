package com.gravitycode.simpletracker.app

import com.gravitycode.simpletracker.workout_list.WorkoutListComponent
import dagger.Module

@Module(subcomponents = [WorkoutListComponent::class])
class SubcomponentsModule {}