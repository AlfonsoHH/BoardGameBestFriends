package com.example.alfonsohernandez.boardgamebestfriends.domain.repository

import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import com.google.firebase.storage.UploadTask
import durdinapps.rxfirebase2.RxFirebaseStorage
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class ImageRepository {

    val firebaseInstance = FirebaseStorage.getInstance().getReference("images")

    fun savePhoto(name: String, data: ByteArray): Single<UploadTask.TaskSnapshot> {
        return RxFirebaseStorage.putBytes(firebaseInstance.child(name),data)
    }

}