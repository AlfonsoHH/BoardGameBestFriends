package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.components

import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.ActivityScope
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting.AddMeetingActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groups.GroupsFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail.GameDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.places.PlacesFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings.MeetingsFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail.PlaceDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.profile.ProfileFragment
import com.example.alfonsohernandez.boardgamebestfriends.presentation.signup.SignUpActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.splash.SplashActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMHandler
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target: SplashActivity)
    fun inject(target: LoginActivity)
    fun inject(target: SignUpActivity)
    fun inject(target: TabActivity)
    fun inject(target: MeetingsFragment)
    fun inject(target: GroupsFragment)
    fun inject(target: PlacesFragment)
    fun inject(target: ProfileFragment)
    fun inject(target: ChatActivity)
    fun inject(target: GamesActivity)
    fun inject(target: PlaceDetailActivity)
    fun inject(target: MeetingDetailActivity)
    fun inject(target: GroupDetailActivity)
    fun inject(target: GameDetailActivity)
    fun inject(target: AddMeetingActivity)
    fun inject(target: AddGroupActivity)
    fun inject(target: AddPlaceActivity)

}