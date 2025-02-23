package com.firebaseserviceandroidapp.features.home.data.repository

import com.firebaseserviceandroidapp.core.constants.Constants
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.features.home.data.models.NoteResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {

    fun getNotesRealTime(date: String): Flow<NoteResult> = callbackFlow {
        val userId = firebaseAuth.currentUser?.uid
            ?: return@callbackFlow

        val listener = fireStore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .collection(Constants.COLLECTION_NOTES)
            .whereEqualTo(Constants.COLLECTION_CHOOSE_DATA, date)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(NoteResult.Error(e.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                val notes = snapshot?.toObjects<NoteItem>() ?: emptyList()
                trySend(NoteResult.Success(notes))
            }

        awaitClose { listener.remove() }
    }

    suspend fun deleteNoteById(noteId: String): NoteResult {
        val userId = firebaseAuth.currentUser?.uid
            ?: return NoteResult.Error("User not authenticated")

        return try {
            fireStore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_NOTES)
                .document(noteId)
                .delete()
                .await()
            NoteResult.Success(emptyList()) // Success response
        } catch (e: Exception) {
            NoteResult.Error(e.localizedMessage ?: "Error deleting note")
        }
    }
}
