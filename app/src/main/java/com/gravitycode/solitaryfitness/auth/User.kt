package com.gravitycode.solitaryfitness.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

data class User(
    val id: String,
    val name: String?,
    val email: String?,
    val profilePicture: Uri?
) {

    constructor(id: String, name: String?, email: String?, profilePictureUrl: String) : this(
        id = id,
        name = name,
        email = email,
        profilePicture = Uri.parse(profilePictureUrl)
    )

    constructor(firebaseUser: FirebaseUser) : this(
        id = firebaseUser.uid,
        name = firebaseUser.displayName,
        email = firebaseUser.email,
        profilePicture = firebaseUser.photoUrl
    )
}