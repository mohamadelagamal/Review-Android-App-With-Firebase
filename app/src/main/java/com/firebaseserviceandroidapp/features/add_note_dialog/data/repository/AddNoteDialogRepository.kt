package com.firebaseserviceandroidapp.features.add_note_dialog.data.repository

import com.firebaseserviceandroidapp.core.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddNoteDialogRepository @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {

    fun addNote(
        title: String,
        details: String,
        status: String,
        chooseDate: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val noteId = firebaseFireStore.collection(Constants.COLLECTION_USERS)
                .document(currentUser.uid)
                .collection(Constants.COLLECTION_NOTES)
                .document().id

            val note = hashMapOf(
                "title" to title,
                "titleLowercase" to title.lowercase(), // تخزين العنوان بحروف صغيرة
                "description" to details,
                "descriptionLowercase" to details.lowercase(), // تخزين الوصف بحروف صغيرة
                "status" to status,
                "chooseDate" to chooseDate,
                "updated_at" to System.currentTimeMillis(),
                "userId" to currentUser.uid,
                "id" to noteId
            )

            firebaseFireStore.collection(Constants.COLLECTION_USERS)
                .document(currentUser.uid)
                .collection(Constants.COLLECTION_NOTES)
                .document(noteId)
                .set(note)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                }
        } else {
            onError("User not authenticated")
        }
    }

}