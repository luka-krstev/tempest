package com.elfak.tempest.presentation.shared.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.presentation.shared.preferences.AuthPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var state: AuthState by mutableStateOf(AuthState.Idle)
        private set

    fun signIn(email: String, password: String) {
        state = AuthState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        state = AuthState.Success("You signed in successfully.")
                        AuthPreferences.setUserID(task.result.user?.uid)
                    } else {
                        state = AuthState.Error(task.exception?.message.toString())
                    }
                }
        }
    }

    fun signUp(
        email: String,
        password: String,
        phone: String,
        name: String,
        username: String
    ) {
        state = AuthState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        val user = task.result.user
                        val payload = mapOf(
                            "email" to email,
                            "name" to name,
                            "username" to username,
                            "avatar" to null,
                            "phone" to phone
                        )

                        if (user == null) {
                            state = AuthState.Error("Something went wrong.")
                        } else {
                            firestore.collection("users").document(user.uid).set(payload)
                                .addOnSuccessListener {
                                    state = AuthState.Success("You signed up successfully.")
                                    AuthPreferences.setUserID(user.uid)
                                }
                                .addOnFailureListener {
                                    state = AuthState.Error("Something went wrong.")
                                }
                        }
                    } else {
                        state = AuthState.Error(task.exception?.message.toString())
                    }
                }
        }
    }

    fun signOut(onSignOut: () -> Unit) {
        auth.signOut()
        AuthPreferences.clearUserData()
        onSignOut()
    }

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}