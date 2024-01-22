package com.gravitycode.solitaryfitness.app

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module(subcomponents = [ActivityComponent::class])
object ApplicationModule {

    @Provides
    @ApplicationScope
    fun providesApplicationExecutor(): Executor = Executors.newSingleThreadExecutor()

    @Provides
    @ApplicationScope
    fun providesApplicationScope() = CoroutineScope(Dispatchers.Main + SupervisorJob())
}