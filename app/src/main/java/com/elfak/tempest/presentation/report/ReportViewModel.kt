package com.elfak.tempest.presentation.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Date

data class Report(
    val title: String,
    val content: String,
    val priority: String,
    val timeCreated: Date,
    val timeEdited: Date?,
    val userEmail: String,
    val latitude: Float,
    val longitude: Float,
)

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
                timeEdited = null,
                userEmail = user.email ?: "Unknown",
                latitude = latitude,
                longitude = longitude
            )

            viewModelScope.launch {
                state = ReportState.Loading
                firestore.collection("reports")
                    .add(report)
                    .addOnSuccessListener {
                        state = ReportState.Success("Report created successfully.")
                    }
                    .addOnFailureListener { e ->
                        state = ReportState.Error("Report creation failed.")
                        e.printStackTrace()
                    }
            }
        }
    }

    sealed class ReportState {
        data object Idle : ReportState()
        data object Loading : ReportState()
        data class Success(val message: String) : ReportState()
        data class Error(val message: String) : ReportState()
    }
}
