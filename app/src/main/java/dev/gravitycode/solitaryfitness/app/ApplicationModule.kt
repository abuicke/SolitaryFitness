package dev.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dev.gravitycode.caimito.kotlin.net.InternetMonitor
import dev.gravitycode.solitaryfitness.util.data.DataStoreManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
object ApplicationModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun providesApplicationScope() = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun providesInternetMonitor(
        applicationScope: CoroutineScope,
        connectivityManager: ConnectivityManager
    ): InternetMonitor {
        return InternetMonitor.getInstance(applicationScope, connectivityManager)
    }

    @Provides
    fun providesDataStoreManager(context: Context) = DataStoreManager.getInstance(context)
}