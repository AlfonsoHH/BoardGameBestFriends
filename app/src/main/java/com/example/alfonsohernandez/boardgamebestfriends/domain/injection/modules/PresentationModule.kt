package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.ActivityScope
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.GetMessagesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.SendMessageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting.AddMeetingContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting.AddMeetingPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlacePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail.GameDetailContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail.GameDetailPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groups.GroupsContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groups.GroupsPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings.MeetingsContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings.MeetingsPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail.PlaceDetailContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail.PlaceDetailPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.places.PlacesContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.places.PlacesPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.profile.ProfileContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.profile.ProfilePresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.signup.SignUpContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.signup.SignUpPresenter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabContract
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabPresenter
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {
    @Provides
    @ActivityScope
    fun providesTabPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                             getAllRegionInteractor: GetAllRegionInteractor,
                             modifyUserInteractor: ModifyUserInteractor,
                             saveUserProfileInteractor: SaveUserProfileInteractor,
                             newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): TabContract.Presenter {
        return TabPresenter(getUserProfileInteractor,
                getAllRegionInteractor,
                modifyUserInteractor,
                saveUserProfileInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesMeetingsPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                  getOpenMeetingsInteractor: GetOpenMeetingsInteractor,
                                  getUserMeetingsInteractor: GetUserMeetingsInteractor,
                                  getUserGroupsInteractor: GetUserGroupsInteractor,
                                  getUserPlacesInteractor: GetUserPlacesInteractor,
                                  getGroupMeetingsInteractor: GetGroupMeetingsInteractor,
                                  getPlaceMeetingsInteractor: GetPlaceMeetingsInteractor,
                                  removeMeetingInteractor: RemoveMeetingInteractor): MeetingsContract.Presenter {
        return MeetingsPresenter(getUserProfileInteractor,
                getOpenMeetingsInteractor,
                getUserMeetingsInteractor,
                getUserGroupsInteractor,
                getUserPlacesInteractor,
                getGroupMeetingsInteractor,
                getPlaceMeetingsInteractor,
                removeMeetingInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGroupsPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                getUserGroupsInteractor: GetUserGroupsInteractor,
                                getSingleGroupInteractor: GetSingleGroupInteractor,
                                removeGroupInteractor: RemoveGroupInteractor,
                                removeGroupToUserInteractor: RemoveGroupToUserInteractor): GroupsContract.Presenter {
        return GroupsPresenter(getUserProfileInteractor,
                getUserGroupsInteractor,
                getSingleGroupInteractor,
                removeGroupInteractor,
                removeGroupToUserInteractor)
    }

    @Provides
    @ActivityScope
    fun providesPlacesPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                getOpenPlacesInteractor: GetOpenPlacesInteractor): PlacesContract.Presenter {
        return PlacesPresenter(getUserProfileInteractor,
                getOpenPlacesInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGamesPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                               getAllGamesInteractor: GetAllGamesInteractor,
                               getUserGamesInteractor: GetUserGamesInteractor,
                               getGroupGamesInteractor: GetGroupGamesInteractor,
                               getPlaceGamesInteractor: GetPlaceGamesInteractor,
                               getSingleGameInteractor: GetSingleGameInteractor,
                               getBggGameCollectionInteractor: GetBggGameCollectionInteractor,
                               addGameInteractor: AddGameInteractor,
                               addGameToUserInteractor: AddGameToUserInteractor,
                               addGameToGroupInteractor: AddGameToGroupInteractor,
                               addGameToPlaceInteractor: AddGameToPlaceInteractor,
                               removeGameToUserInteractor: RemoveGameToUserInteractor,
                               removeGameToGroupInteractor: RemoveGameToGroupInteractor,
                               removeGameToPlaceInteractor: RemoveGameToPlaceInteractor,
                               newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GamesContract.Presenter {
        return GamesPresenter(getUserProfileInteractor,
                getAllGamesInteractor,
                getUserGamesInteractor,
                getGroupGamesInteractor,
                getPlaceGamesInteractor,
                getSingleGameInteractor,
                getBggGameCollectionInteractor,
                addGameInteractor,
                addGameToUserInteractor,
                addGameToGroupInteractor,
                addGameToPlaceInteractor,
                removeGameToUserInteractor,
                removeGameToGroupInteractor,
                removeGameToPlaceInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesMeetingDetailPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                       getSingleMeetingInteractor: GetSingleMeetingInteractor,
                                       getSingleGameInteractor: GetSingleGameInteractor,
                                       getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                       getMeetingUsersInteractor: GetMeetingUsersInteractor,
                                       getSingleUserInteractor: GetSingleUserInteractor,
                                       addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                       modifyMeetingInteractor: ModifyMeetingInteractor,
                                       removeMeetingToUserInteractor: RemoveMeetingToUserInteractor,
                                       firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): MeetingDetailContract.Presenter {
        return MeetingDetailPresenter(getUserProfileInteractor,
                getSingleMeetingInteractor,
                getSingleGameInteractor,
                getSinglePlaceInteractor,
                getMeetingUsersInteractor,
                getSingleUserInteractor,
                addMeetingToUserInteractor,
                modifyMeetingInteractor,
                removeMeetingToUserInteractor,
                firebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGroupDetailPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                     addGroupToUserInteractor: AddGroupToUserInteractor,
                                     getSingleGroupInteractor: GetSingleGroupInteractor,
                                     getRegionInteractor: GetRegionInteractor,
                                     getGroupUsersInteractor: GetGroupUsersInteractor,
                                     getSingleUserInteractor: GetSingleUserInteractor,
                                     newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GroupDetailContract.Presenter {
        return GroupDetailPresenter(getUserProfileInteractor,
                addGroupToUserInteractor,
                getSingleGroupInteractor,
                getRegionInteractor,
                getGroupUsersInteractor,
                getSingleUserInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesChatPresenter(profileInteractor: GetUserProfileInteractor,
                              getMessagesInteractor: GetMessagesInteractor,
                              sendMessageInteractor: SendMessageInteractor,
                              newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ChatContract.Presenter {
        return ChatPresenter(profileInteractor,
                getMessagesInteractor,
                sendMessageInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesPlaceDetailPresenter(userProfileInteractor: GetUserProfileInteractor,
                                     getRegionInteractor: GetRegionInteractor,
                                     getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                     firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): PlaceDetailContract.Presenter {
        return PlaceDetailPresenter(userProfileInteractor,
                getRegionInteractor,
                getSinglePlaceInteractor,
                firebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGameDetailPresenter(getSingleGameInteractor: GetSingleGameInteractor,
                                    newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GameDetailContract.Presenter {
        return GameDetailPresenter(getSingleGameInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesAddGroupPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                  getAllUsersInteractor: GetAllUsersInteractor,
                                  getSingleUserFromMailInteractor: GetSingleUserFromMailInteractor,
                                  getSingleGroupInteractor: GetSingleGroupInteractor,
                                  addGroupInteractor: AddGroupInteractor,
                                  modifyGroupInteractor: ModifyGroupInteractor,
                                  newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddGroupContract.Presenter {
        return AddGroupPresenter(getUserProfileInteractor,
                getAllUsersInteractor,
                getSingleUserFromMailInteractor,
                getSingleGroupInteractor,
                addGroupInteractor,
                modifyGroupInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesAddMeetingPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                    addMeetingInteractor: AddMeetingInteractor,
                                    addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                    addMeetingToGroupInteractor: AddMeetingToGroupInteractor,
                                    addMeetingToPlaceInteractor: AddMeetingToPlaceInteractor,
                                    getUserGroupsInteractor: GetUserGroupsInteractor,
                                    getSingleGroupInteractor: GetSingleGroupInteractor,
                                    getOpenPlacesInteractor: GetOpenPlacesInteractor,
                                    getUserPlacesInteractor: GetUserPlacesInteractor,
                                    getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                    getUserGamesInteractor: GetUserGamesInteractor,
                                    getGroupGamesInteractor: GetGroupGamesInteractor,
                                    getPlaceGamesInteractor: GetPlaceGamesInteractor,
                                    getSingleGameInteractor: GetSingleGameInteractor,
                                    newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddMeetingContract.Presenter {
        return AddMeetingPresenter(getUserProfileInteractor,
                addMeetingInteractor,
                addMeetingToUserInteractor,
                addMeetingToGroupInteractor,
                addMeetingToPlaceInteractor,
                getUserGroupsInteractor,
                getSingleGroupInteractor,
                getOpenPlacesInteractor,
                getUserPlacesInteractor,
                getSinglePlaceInteractor,
                getUserGamesInteractor,
                getGroupGamesInteractor,
                getPlaceGamesInteractor,
                getSingleGameInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesAddPlacePresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                  addPlaceInteractor: AddPlaceInteractor,
                                  addPlaceToUserInteractor: AddPlaceToUserInteractor,
                                  modifyPlaceInteractor: ModifyPlaceInteractor,
                                  getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                  getRegionInteractor: GetRegionInteractor,
                                  newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddPlaceContract.Presenter {
        return AddPlacePresenter(getUserProfileInteractor,
                addPlaceInteractor,
                addPlaceToUserInteractor,
                modifyPlaceInteractor,
                getSinglePlaceInteractor,
                getRegionInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesLoginPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                               saveUserProfileInteractor: SaveUserProfileInteractor,
                               getSingleUserInteractor: GetSingleUserInteractor,
                               getAllUsersInteractor: GetAllUsersInteractor,
                               addUserInteractor: AddUserInteractor,
                               modifyUserInteractor: ModifyUserInteractor,
                               getAllRegionInteractor: GetAllRegionInteractor,
                               newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): LoginContract.Presenter {
        return LoginPresenter(getUserProfileInteractor,
                saveUserProfileInteractor,
                getSingleUserInteractor,
                getAllUsersInteractor,
                addUserInteractor,
                modifyUserInteractor,
                getAllRegionInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesSignUpPresenter(getAllUsersInteractor: GetAllUsersInteractor,
                                addUserInteractor: AddUserInteractor,
                                getAllRegionInteractor: GetAllRegionInteractor,
                                newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): SignUpContract.Presenter {
        return SignUpPresenter(getAllUsersInteractor,
                addUserInteractor,
                getAllRegionInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesProfilePresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                 getUserPlacesInteractor: GetUserPlacesInteractor,
                                 getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                 getRegionInteractor: GetRegionInteractor,
                                 newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ProfileContract.Presenter {
        return ProfilePresenter(getUserProfileInteractor,
                getUserPlacesInteractor,
                getSinglePlaceInteractor,
                getRegionInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

}