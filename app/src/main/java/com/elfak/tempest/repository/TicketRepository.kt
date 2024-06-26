package com.elfak.tempest.repository

import com.elfak.tempest.model.Ticket
import com.elfak.tempest.presentation.filter.FilterState
import com.elfak.tempest.utility.Response
import com.elfak.tempest.utility.location.calculateBoundingBox
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TicketRepository {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val userRepository = UserRepository()

    fun get(ticketId: String): Flow<Response<Ticket?>> = callbackFlow {
        trySend(Response.Loading)

        val documentRef = db.collection("tickets").document(ticketId)
        val listenerRegistration = documentRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                trySend(Response.Failure(error))
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val ticket = documentSnapshot.toObject(Ticket::class.java)
                if (ticket != null) {
                    trySend(Response.Success(ticket.copy(id = documentSnapshot.id)))
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

    fun get(filter: FilterState = FilterState(), user: Pair<Double, Double>?): Flow<Response<List<Ticket>>> = callbackFlow {
        trySend(Response.Loading)

        var query: Query = db.collection("tickets")

        if (filter.title.isNotEmpty()) {
            query = query.whereEqualTo("title", filter.title)
        }
        if (filter.content.isNotEmpty()) {
            query = query.whereEqualTo("content", filter.content)
        }
        if (filter.priority.isNotEmpty()) {
            query = query.whereEqualTo("priority", filter.priority)
        }
        if (filter.author.email.isNotEmpty()) {
            query = query.whereEqualTo("userEmail", filter.author.email)
        }
        if (filter.created != null) {
            query = query.whereGreaterThan("timeCreated", Timestamp(filter.created))
        }

        if (filter.radius != null) {
            val (southwest, northeast) = calculateBoundingBox(user, filter.radius.toDouble())
            query = query.whereGreaterThanOrEqualTo("latitude", southwest.latitude)
                .whereLessThanOrEqualTo("latitude", northeast.latitude)
                .whereGreaterThanOrEqualTo("longitude", southwest.longitude)
                .whereLessThanOrEqualTo("longitude", northeast.longitude)
        }

        val listenerRegistration = query.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                trySend(Response.Failure(error))
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                val tickets = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Ticket::class.java)?.copy(id = document.id)
                }
                trySend(Response.Success(tickets))
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun save(ticket: Ticket): Flow<Response<Ticket>> = callbackFlow {
        trySend(Response.Loading)
        val documentRef = if (ticket.id.isEmpty()) {
            db.collection("tickets").document()
        } else {
            db.collection("tickets").document(ticket.id)
        }

        documentRef.set(ticket).addOnSuccessListener {
            trySend(Response.Success(ticket.copy(id = documentRef.id)))
        }.addOnFailureListener { exception ->
            trySend(Response.Failure(exception))
        }

        awaitClose { }
    }

    fun solve(id: String, onSuccess: () -> Unit = { }) {
        val reference = db.collection("tickets").document(id)
        reference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val email = documentSnapshot.getString("userEmail")
                    if (email != null) {
                        reference.update("solved", true)
                            .addOnSuccessListener {
                                userRepository.points(email, 50,
                                    onSuccess = {
                                        onSuccess()
                                    },
                                )
                            }
                    }
                }
            }
    }
}