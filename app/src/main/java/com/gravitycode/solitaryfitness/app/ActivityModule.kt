package com.gravitycode.solitaryfitness.app

import android.content.Context
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.di.ActivityScope
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module
object ActivityModule {

    @Provides
    fun providesApplicationContext(activity: ComponentActivity): Context = activity.applicationContext

    @Provides
    fun providesAppController(activity: ComponentActivity) = activity as AppController

    @Provides
    @ActivityScope
    fun providesToaster(context: Context) = Toaster(context)
}