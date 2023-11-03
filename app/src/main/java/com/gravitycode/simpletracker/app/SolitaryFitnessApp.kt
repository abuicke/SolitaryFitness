package com.gravitycode.solitaryfitness.app

import android.app.Application

class SolitaryFitnessApp : Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
}