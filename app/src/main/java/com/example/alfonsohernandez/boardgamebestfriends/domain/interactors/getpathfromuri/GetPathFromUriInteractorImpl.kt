package com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri

import android.net.Uri
import com.example.alfonsohernandez.boardgamebestfriends.domain.contentresolver.GetPathFromUri

class GetPathFromUriInteractorImpl(private val getPathFromUri: GetPathFromUri): GetPathFromUriInteractor {
    override fun getPathFromUri(contentUri: Uri): String {
        return getPathFromUri.checkVersion(contentUri)
    }
}