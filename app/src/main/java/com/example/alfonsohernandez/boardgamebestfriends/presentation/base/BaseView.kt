package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

interface BaseView {

    fun showError(stringId: Int)
    fun showSuccess(stringId: Int)
    fun showProgress(isLoading: Boolean)

}