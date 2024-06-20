package com.elfak.tempest.presentation.shared.view_models

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.helpers.DateParceler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.util.Date

@Parcelize
@TypeParceler<Date, DateParceler>
data class Report(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val priority: String = "",
    val timeCreated: Date = Date(),
    val userEmail: String = "",
    val solved: Boolean = false,
    val latitude: Float = 0f,
    val longitude: Float = 0f,
) : Parcelable

class ReportViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var state: ReportState by mutableStateOf(ReportState.Idle)
        private set

    fun createReport(title: String, content: String, priority: String, latitude: Float, longitude: Float) {
        val user = auth.currentUser
        if (user != null) {
            val report = Report(
                title = title,
                content = content,
                priority = priority,
                timeCreated = Date(),
                userEmail = user.email ?: "Unknown",
                latitude = latitude,
                longitude = longitude
            )

            viewModelScope.launch {
                state = ReportState.Loading
                firestore.collection("reports")
                    .add(report)
                    .addOnSuccessListener {
                        val userRef = firestore.collection("users").document(user.uid)
                        firestore.runTransaction { transaction ->
                            val snapshot = transaction.get(userRef)
                            val currentPoints = snapshot.getLong("points") ?: 0
                            transaction.update(userRef, "points", currentPoints + 50)
                        }.addOnSuccessListener {
                            state =
                                ReportState.Success("Report created successfully, and points updated.")
                        }.addOnFailureListener { exception ->
                            state =
                                ReportState.Error("Report created, but failed to update points.")
                            exception.printStackTrace()
                        }                    }
                    .addOnFailureListener { exception ->
                        state = ReportState.Error("Report creation failed.")
                        exception.printStackTrace()
                    }
            }
        }
    }

    fun updateReport(report: String, newTitle: String, newContent: String, newPriority: String) {
        val user = auth.currentUser
        if (user != null) {
            val reportRef = firestore.collection("reports").document(report)

            viewModelScope.launch {
                state = ReportState.Loading

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(reportRef)

                    transaction.update(reportRef, "title", newTitle)
                    transaction.update(reportRef, "content", newContent)
                    transaction.update(reportRef, "priority", newPriority)

                    val userRef = firestore.collection("users").document(user.uid)
                    val currentPoints = snapshot.getLong("points") ?: 0
                    transaction.update(userRef, "points", currentPoints + 50)
                }.addOnSuccessListener {
                    state = ReportState.Success("Report updated successfully, and points updated.")
                }.addOnFailureListener { exception ->
                    state = ReportState.Error("Failed to update report or award points.")
                    exception.printStackTrace()
                }
            }
        }
    }

    fun fetchAllReports(): Flow<List<Report>> = callbackFlow {
        val reportsCollection = firestore.collection("reports")

        val listenerRegistration: ListenerRegistration = reportsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val reports = snapshot?.documents?.mapNotNull { document ->
                document.toObject(Report::class.java)?.let { report ->
                    Report(
                        id = document.id,
                        title = report.title,
                        content = report.content,
                        priority = report.priority,
                        timeCreated = report.timeCreated,
                        userEmail = report.userEmail,
                        latitude = report.latitude,
                        longitude = report.longitude,
                        solved = report.solved
                    )
                }
            }.orEmpty()
            trySend(reports)
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun fetchReportByUid(uid: String, onSuccess: (Report?) -> Unit) {
        val reportDocument = firestore.collection("reports").document(uid)

        reportDocument.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val report = document.toObject(Report::class.java)
                    onSuccess(report)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    fun solveReport(report: String) {
        val currentUser = auth.currentUser
        val userEmail = currentUser!!.email

        firestore.collection("users").whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val userId = userDocument.id

                    val reportRef = firestore.collection("reports").document(report)
                    reportRef.update("solved", true)
                        .addOnSuccessListener {
                            val userRef = firestore.collection("users").document(userId)
                            userRef.update("points", FieldValue.increment(100))
                        }
                        .addOnFailureListener { exception ->
                            exception.printStackTrace()
                        }
                } else {
                    println("User not found")
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    sealed class ReportState {
        data object Idle : ReportState()
        data object Loading : ReportState()
        data class Success(val message: String) : ReportState()
        data class Error(val message: String) : ReportState()
    }
}
