package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

interface MeetingDetailContract {

    interface View {

        fun setMeetingData(meeting: Meeting)
        fun setGameData(game: Game)
        fun setPlaceData(place: Place)
        fun clearFriendsData()
        fun setFriendData(friend: User, itsTheUser: Boolean)
        fun setupRecycler()

        fun showErrorJoining()
        fun showErrorLeaving()
        fun showErrorMeeting()
        fun showErrorPlace()
        fun showErrorGame()
        fun showErrorMembers()
        fun showErrorVacant()

        fun successJoining()
        fun successLeaving()

        fun showProgressBar(boolean: Boolean)
    }

    interface Presenter {

        fun getUserProfile(): User?

        fun joinGame(boolean: Boolean)
        fun getMeetingOpenHours(place: Place): String

//        fun assistingMeeting(meetingId: String): Boolean

        fun getMeetingData(meetingId: String)
        fun getGameData(gameId: String)
        fun getPlaceData(placeId: String)

        fun getFriendsData(meetingId: String)
        fun getFriendData(friendId: String)

        fun firebaseEvent(id: String, activityName: String)

    }

}