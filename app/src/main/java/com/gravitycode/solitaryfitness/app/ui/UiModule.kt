package com.gravitycode.solitaryfitness.app.ui

import android.content.Context
import com.gravitycode.solitaryfitness.di.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
object UiModule {

    @Provides
    @ApplicationScope
    fun providesToaster(context: Context) = Toaster(context)
}