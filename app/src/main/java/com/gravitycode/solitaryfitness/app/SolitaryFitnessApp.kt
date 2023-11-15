package com.gravitycode.solitaryfitness.app

import android.app.Application
import com.gravitycode.solitaryfitness.di.AppComponent
import com.gravitycode.solitaryfitness.di.DaggerAppComponent

class SolitaryFitnessApp : Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
}