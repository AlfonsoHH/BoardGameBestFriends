package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri

import android.net.Uri

interface GetPathFromUriInteractor {
    fun getPathFromUri(contentUri: Uri): String
}