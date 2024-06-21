package com.elfak.tempest.repository

import android.net.Uri
import com.elfak.tempest.utility.Response
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class StorageRepository {
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    fun upload(path: String, uri: Uri): Flow<Response<Uri>> = callbackFlow {
        trySend(Response.Loading)

        val image = storage.reference.child(path)
        val upload = image.putFile(uri)
        upload.addOnSuccessListener { task ->
            task.storage.downloadUrl.addOnSuccessListener {uri ->
                trySend(Response.Success(uri))
            }
        }.addOnFailureListener { exception ->
            trySend(Response.Failure(exception))
        }

        awaitClose {  }
    }
}