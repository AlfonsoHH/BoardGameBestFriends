package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings

import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseView

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
interface MeetingsContract {

    interface View: BaseView {

        fun setData(meetings: Collection<Meeting>?)
        fun getSearchData():String?

    }

    interface Presenter {

        fun removeMeeting(meetingId: String)
        fun initialDataChooser()
        fun cacheDataChooser()
    }

}