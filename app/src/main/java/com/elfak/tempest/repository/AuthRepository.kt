package com.elfak.tempest.repository

import com.elfak.tempest.model.User
import com.elfak.tempest.utility.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun register(email: String, password: String): Flow<Response<Unit>> = callbackFlow {
        trySend(Response.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                trySend(Response.Success(Unit))
            }
            .addOnFailureListener { exception ->
                trySend(Response.Failure(exception))
            }

        awaitClose {  }
    }

    fun login(email: String, password: String): Flow<Response<Unit>> = callbackFlow {
        trySend(Response.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                trySend(Response.Success(Unit))
            }
            .addOnFailureListener { exception ->
                trySend(Response.Failure(exception))
            }

        awaitClose {  }
    }

    fun current(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout() {
        auth.signOut()
    }
}