package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.gravitycode.solitaryfitness.util.android.Toaster
import com.gravitycode.solitaryfitness.util.android.data.DataStoreManager
import com.gravitycode.solitaryfitness.util.net.InternetMonitor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope

@Module
object AndroidModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun providesNetworkStateObservable(
        applicationScope: CoroutineScope,
        connectivityManager: ConnectivityManager
    ): InternetMonitor {
        return InternetMonitor.getInstance(applicationScope, connectivityManager)
    }

    @Provides
    fun providesDataStoreManager(context: Context) = DataStoreManager.getInstance(context)

    @Provides
    @ApplicationScope
    fun providesToaster(context: Context) = Toaster.create(context)
}