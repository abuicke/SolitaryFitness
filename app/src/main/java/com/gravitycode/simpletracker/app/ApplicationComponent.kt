package com.gravitycode.simpletracker.app

import com.gravitycode.simpletracker.workout_list.WorkoutListActivity
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    /**
     * TODO: Can this function be called anything? Are all functions exposed by components automatically injection sites?
     *
     * TODO: Should this injection be happening here? Shouldn't it be happening in
     * [com.gravitycode.simpletracker.workout_list.WorkoutListComponent]? Can I share [ApplicationComponent]
     * as a parent to all the feature components, e.g. [com.gravitycode.simpletracker.workout_list.WorkoutListComponent]?
     *
     * Subcomponents are components that inherit and extend the object graph of a parent component.
     * You can use them to partition your application’s object graph into subgraphs either to
     * encapsulate different parts of your application from each other or to use more than one scope
     * within a component.
     *
     * But the application context from [ApplicationComponent] gets injected into
     * [com.gravitycode.simpletracker.workout_list.WorkoutListComponent] without any problems?
     * So what exactly does a subcomponent actually do?
     *
     * [https://khreniak.medium.com/dagger-scopes-simple-explanation-184684707227]
     * */
    fun inject(activity: WorkoutListActivity)
}