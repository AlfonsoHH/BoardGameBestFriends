package com.example.alfonsohernandez.boardgamebestfriends.domain

import android.view.View

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
