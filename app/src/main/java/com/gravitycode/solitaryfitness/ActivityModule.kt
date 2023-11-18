package com.gravitycode.solitaryfitness

import android.content.Context
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.app.AppController
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module
object ActivityModule {

    @Provides
    fun providesApplicationContext(activity: ComponentActivity): Context = activity.applicationContext

    @Provides
    fun providesToaster(context: Context) = Toaster(context)

    @Provides
    fun providesAppController(activity: ComponentActivity) = activity as AppController
}