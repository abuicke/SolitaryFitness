package com.gravitycode.solitaryfitness.track_reps

import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.app.ActivityScope
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [TrackRepsModule::class])
interface TrackRepsComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance fun componentActivity(activity: ComponentActivity): Builder

        fun build(): TrackRepsComponent
    }

    fun inject(activity: MainActivity)
}