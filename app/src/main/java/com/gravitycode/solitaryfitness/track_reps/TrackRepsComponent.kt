package com.gravitycode.solitaryfitness.track_reps

import com.gravitycode.solitaryfitness.app.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [TrackRepsModule::class])
interface TrackRepsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TrackRepsComponent
    }

    fun inject(activity: TrackRepsActivity)
}