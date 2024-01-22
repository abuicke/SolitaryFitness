package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.gravitycode.solitaryfitness.util.data.DataStoreManager
import com.gravitycode.solitaryfitness.util.net.NetworkStateObservable
import com.gravitycode.solitaryfitness.util.ui.Messenger
import dagger.Module
import dagger.Provides

@Module
object AndroidModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun providesNetworkStateObservable(connectivityManager: ConnectivityManager): NetworkStateObservable {
        return NetworkStateObservable.getInstance(connectivityManager)
    }

    @Provides
    fun providesDataStoreManager(context: Context) = DataStoreManager.getInstance(context)

    @Provides
    @ApplicationScope
    fun providesMessenger(context: Context) = Messenger.create(context)
}