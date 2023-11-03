package com.gravitycode.solitaryfitness.app

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ApplicationScope()

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope()