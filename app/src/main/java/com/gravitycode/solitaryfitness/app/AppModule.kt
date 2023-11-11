package com.gravitycode.solitaryfitness.app

import android.app.Application
import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.gravitycode.solitaryfitness.util.ui.Toaster
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ActivityComponent::class])
object AppModule {

    @Provides
    fun providesApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun providesToaster(context: Context) = Toaster(context)

    @Provides
    fun providesFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {
                setSizeBytes(10)
            })
        }

        return firestore
    }

    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesFirebaseAuthUi() = AuthUI.getInstance()
}