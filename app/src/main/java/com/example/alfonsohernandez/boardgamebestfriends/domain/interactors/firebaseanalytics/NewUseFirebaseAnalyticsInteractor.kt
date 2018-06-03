package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics

/**
 * Created by alfonsohernandez on 28/03/2018.
 */
interface NewUseFirebaseAnalyticsInteractor {
    fun sendingDataFirebaseAnalytics(id: String, activityName: String)
}