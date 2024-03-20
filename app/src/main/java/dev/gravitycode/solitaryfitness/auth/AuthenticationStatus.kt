package dev.gravitycode.solitaryfitness.auth

import kotlinx.coroutines.flow.Flow

interface AuthenticationStatus {

    val authState: Flow<AuthState>
}