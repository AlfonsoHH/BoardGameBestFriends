package com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationView
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface AddMeetingContract {

    interface View: BaseNotificationView {

        fun addGameToSpinner(gamesTitle: String)
        fun addGamesToSpinner(games: ArrayList<Game>)
        fun itsGame(game: String): Boolean

        fun setData(meeting: Meeting, place: Place, group:Group,game:Game)

        fun finishAddMeeting()

    }

    interface Presenter{

        fun saveMeeting(meeting: Meeting, playing: Boolean)
        fun modifyMeeting(meeting: Meeting, playing: Boolean)

        fun getMyPlacee(): Place?
        fun getMeetingData(meetingId: String)

        fun getUserGroups(): ArrayList<String>
        fun getPlaces(): ArrayList<String>

        fun getUserGames(userId: String)
        fun getGroupGames(groupId: String?)
        fun getPlaceGames(placeId: String?)

        fun getGroupFromTitle(groupTitle: String): Group?
        fun getPlaceFromTitle(placeName: String): Place?
        fun getGameFromTitle(gameTitle: String): Game

    }
}