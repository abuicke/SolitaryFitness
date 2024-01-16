package com.gravitycode.solitaryfitness.app

import android.content.Context
import androidx.activity.ComponentActivity
import com.gravitycode.solitaryfitness.di.ApplicationScope
import com.gravitycode.solitaryfitness.util.ui.Messenger
import dagger.Module
import dagger.Provides

@Module
object ApplicationModule {

    @Provides
    fun providesApplicationContext(activity: ComponentActivity): Context = activity.applicationContext

    @Provides
    fun providesAppController(activity: ComponentActivity) = activity as AppController

    @Provides
    @ApplicationScope
    fun providesMessenger(context: Context) = Messenger.create(context)
}