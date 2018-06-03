package com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface AddMeetingContract {

    interface View{

        fun addGroupToSpinner(groupsTitle: String)
        fun addGameToSpinner(gamesTitle: String)
        fun itsGame(game: String): Boolean
        fun setPlaceSpinner(placesList: ArrayList<String>)
        fun finishAddMeeting()

        fun showErrorAdding()
        fun showErrorGames()
        fun showErrorGroups()
        fun showErrorPlaces()
        fun showErrorAddingMeeting()
        fun showErrorAddingUserToMeeting()
        fun showErrorAddingMeetingToGroup()
        fun showErrorAddingMeetingToPlace()
        fun showErrorEmpty()
        fun showErrorMyPlace()

        fun showProgressBar(boolean: Boolean)

    }

    interface Presenter{

        fun getUserProfile(): User?
        fun getMyPlacee(): Place?

        fun saveMeeting(meeting: Meeting, playing: Boolean)

        fun getUserGroups()
        fun getSingleGroup(groupId: String)

        fun getOpenPlaces()
        fun getUserPlaces()

        fun getUserGames(userId: String)
        fun getGroupGames(groupId: String)
        fun getPlaceGames(placeId: String)

        fun getSinglePlace(placeId: String)
        fun getSingleGame(gameId: String)

        fun getGroupFromTitle(groupTitle: String): Group
        fun getPlaceFromTitle(placeName: String): Place
        fun getGameFromTitle(gameTitle: String): Game

        fun firebaseEvent(id: String, activityName: String)

    }
}