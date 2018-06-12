package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage

import com.google.firebase.storage.UploadTask
import io.reactivex.Single

interface SaveImageFirebaseStorageInteractor {
    fun addFirebaseDataImage(name: String, data: ByteArray): Single<UploadTask.TaskSnapshot>
}