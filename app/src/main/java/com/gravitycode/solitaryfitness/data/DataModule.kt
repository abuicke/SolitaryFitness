package com.gravitycode.solitaryfitness.data

import com.gravitycode.solitaryfitness.di.ActivityScope
import dagger.Module
import dagger.Provides

@Module
object DataModule {

    @Provides
    @ActivityScope
    fun providesSyncDataService(): SyncDataService = SyncFirestoreDataService()
}