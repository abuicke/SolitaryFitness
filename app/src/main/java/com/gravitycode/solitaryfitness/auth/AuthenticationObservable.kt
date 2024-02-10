package com.gravitycode.solitaryfitness.auth

import kotlinx.coroutines.flow.Flow

interface AuthenticationObservable {

    val authState: Flow<AuthState>
}