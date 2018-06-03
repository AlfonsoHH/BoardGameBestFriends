package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface MeetingsContract {

    interface View {

        fun setData(meetings: Collection<Meeting>?)
        fun clearData()
        fun setSingleData(meeting: Meeting)
        fun setupRecycler()
        fun itemDetail(id: String)
        fun getSearchData():String

        fun showErrorLoading()
        fun showErrorRemoving()

        fun successAdding()
        fun successRemoving()

        fun showProgressBar(isLoading:Boolean)
        fun noResult()

//        fun userPlaying()
    }

    interface Presenter {

        fun getUserProfile(): User?

        fun getMeetingsData()
        fun getUserMeetings()
        fun getGroupMeetings()
        fun getPlaceMeetings()

        fun removeMeeting(meetingId: String)

        fun dataChooser()

    }

}