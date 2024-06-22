package com.elfak.tempest.repository

import com.elfak.tempest.model.User
import com.elfak.tempest.utility.Response
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getById(id: String): Flow<Response<User?>> = callbackFlow {
        trySend(Response.Loading)

        val documentRef = db.collection("users").document(id)
        val listenerRegistration = documentRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                trySend(Response.Failure(error))
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    trySend(Response.Success(user.copy(id = documentSnapshot.id)))
                } else {
                    trySend(Response.Success(null))
                }
            } else {
                trySend(Response.Success(null))
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }


    fun getByEmail(email: String): Flow<Response<User?>> = callbackFlow {
        trySend(Response.Loading)

        val query = db.collection("users").whereEqualTo("email", email)
        val listenerRegistration = query.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                trySend(Response.Failure(error))
                return@addSnapshotListener
            }

            if (querySnapshot != null && !querySnapshot.isEmpty) {
                val documentSnapshot = querySnapshot.documents.first()
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    trySend(Response.Success(user.copy(id = documentSnapshot.id)))
                } else {
                    trySend(Response.Success(null))
                }
            } else {
                trySend(Response.Success(null))
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun get(): Flow<Response<List<User>>> = callbackFlow {
        trySend(Response.Loading)
        val listener = db.collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    trySend(Response.Failure(error))
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    val users = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)?.copy(id = document.id)
                    }
                    trySend(Response.Success(users))
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    fun save(user: User): Flow<Response<User>> = callbackFlow {
        trySend(Response.Loading)
        if (user.id.isEmpty()) {
            val documentRef = db.collection("users").document()
            documentRef.set(user.copy(id = documentRef.id)).addOnSuccessListener {
                trySend(Response.Success(user.copy(id = documentRef.id)))
            }.addOnFailureListener { exception ->
                trySend(Response.Failure(exception))
            }
        } else {
            val documentRef = db.collection("users").document(user.id)
            documentRef.set(user).addOnSuccessListener {
                trySend(Response.Success(user))
            }.addOnFailureListener { exception ->
                trySend(Response.Failure(exception))
            }
        }

        awaitClose { }
    }

    fun points(email: String, points: Int, onSuccess: () -> Unit = { }) {
        val query = db.collection("users").whereEqualTo("email", email).limit(1)
        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents.first()
                    val userRef = db.collection("users").document(documentSnapshot.id)

                    userRef.update("points", FieldValue.increment(points.toLong()))
                        .addOnSuccessListener {
                            onSuccess()
                        }
                }
            }
    }
}