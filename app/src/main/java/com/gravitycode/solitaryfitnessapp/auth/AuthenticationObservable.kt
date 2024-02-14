package com.gravitycode.solitaryfitnessapp.auth

import kotlinx.coroutines.flow.Flow

interface AuthenticationObservable {

    val authState: Flow<AuthState>
}