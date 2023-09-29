package com.gravitycode.simpletracker.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * TODO: Can this module be turned into an object? Will [Application] still be injected?
 * TODO: Need to use Android scopes, e.g. @ActivityScope
 * */
@Module
class ApplicationModule {

//    @Provides @Singleton fun providesApplicationContext(app: Application): Context = app
}