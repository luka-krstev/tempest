package com.elfak.tempest.presentation.shared.view_models

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.presentation.shared.preferences.AuthPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val avatar: String = "",
    val email: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val phone: String = "",
    val points: Int = 0,
    val serviceActive: Boolean = false,
    val timestamp: Long = 0,
    val username: String = ""
) : Parcelable

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    var state: AuthState by mutableStateOf(AuthState.Idle)
        private set

    var avatar: AvatarState by mutableStateOf(AvatarState.Idle)
        private set

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getUsers(callback: (List<User>) -> Unit) {
        firestore.collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = mutableListOf<User>()
                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        users.add(user.copy(id = document.id))
                    }
                }
                callback(users)
            }
    }

    fun uploadAvatar(userId: String, imageUri: Uri) {
        avatar = AvatarState.Loading
        val imageRef = storage.reference.child("images/$userId/avatar.jpg")
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { task ->
            task.storage.downloadUrl.addOnSuccessListener {uri ->
                val update = mapOf(
                    "avatar" to uri.toString()
                )

                firestore.collection("users").document(userId).update(update).addOnSuccessListener {
                    avatar = AvatarState.Success("Avatar set successfully.");
                }.addOnFailureListener {
                    avatar = AvatarState.Error(it.message.toString())
                }
            }
        }.addOnFailureListener {
            avatar = AvatarState.Error(it.message.toString())
        }
    }

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

    fun getUserById(userId: String, callback: (user: User?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    if (user != null) {
                        callback(user.copy(id = documentSnapshot.id))
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
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
                            "phone" to phone,
                            "points" to 0
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

    fun hasAvatar(userId: String): Boolean {
        var exists = false;
        viewModelScope.launch {
            val document = firestore.collection("users").document(userId)
            document.get()
                .addOnSuccessListener { user ->
                    if (user != null && user.exists()) {
                        exists = user.getString("avatar") != null
                    }
                }
        }
        return exists
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

    sealed class AvatarState {
        data object Idle : AvatarState()
        data object Loading : AvatarState()
        data class Success(val message: String) : AvatarState()
        data class Error(val message: String) : AvatarState()
    }
}