package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import com.gravitycode.solitaryfitness.util.data.DataStoreManager
import com.gravitycode.solitaryfitness.util.ui.Messenger
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
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun providesApplicationExecutor(): Executor = Executors.newSingleThreadExecutor()

    @Provides
    @ApplicationScope
    fun providesApplicationScope() = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Provides
    fun providesDataStoreManager(context: Context) = DataStoreManager.getInstance(context)

    @Provides
    @ApplicationScope
    fun providesMessenger(context: Context) = Messenger.create(context)
}