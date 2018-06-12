package com.example.alfonsohernandez.boardgamebestfriends.push

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class FCMtoken : FirebaseInstanceIdService() {

    private val TAG = "FCMtoken"

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

    }

}