package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage

import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.ImageRepository
import com.google.firebase.storage.UploadTask
import io.reactivex.Completable
import io.reactivex.Single

class SaveImageFirebaseStorageInteractorImpl (private val imageRepository: ImageRepository): SaveImageFirebaseStorageInteractor {
    override fun addFirebaseDataImage(name: String, data: ByteArray): Single<UploadTask.TaskSnapshot> {
        return imageRepository.savePhoto(name,data)
    }
}