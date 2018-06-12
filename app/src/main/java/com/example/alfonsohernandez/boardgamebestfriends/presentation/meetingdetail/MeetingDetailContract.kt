package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

interface MeetingDetailContract {

    interface View: BaseView {

        fun setMeetingData(meeting: Meeting)
        fun setGameData(game: Game)
        fun setPlaceData(place: Place)
        fun clearFriendsData()
        fun setFriendData(friend: User, itsTheUser: Boolean)
        fun removeFriendData(friend: User, itsTheUser: Boolean)
        fun setupRecycler()

    }

    interface Presenter {

        fun joinGame(boolean: Boolean)
        fun getMeetingOpenHours(place: Place): String

        fun getMeetingData(meetingId: String)
        fun getGameData(gameId: String)
        fun getPlaceData(placeId: String)

        fun getFriendsData(meetingId: String)
        fun getFriendData(friendId: String)

    }

}