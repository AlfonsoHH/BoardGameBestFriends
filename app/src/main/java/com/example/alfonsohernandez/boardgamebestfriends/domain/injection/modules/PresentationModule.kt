package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.scopes.ActivityScope
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.CreateMailUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.LoginWithCredentialsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.LoginWithEmailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.GetMessagesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.SendMessageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggXmlGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail.GetBggGameDetailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getlatlong.GetLatLongInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.ClearTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.SetTopicInteractor
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
                             paperRegionsInteractor: PaperRegionsInteractor,
                             paperGamesInteractor: PaperGamesInteractor,
                             paperMeetingsInteractor: PaperMeetingsInteractor,
                             paperPlacesInteractor: PaperPlacesInteractor,
                             getAllGamesInteractor: GetAllGamesInteractor,
                             modifyUserInteractor: ModifyUserInteractor,
                             saveUserProfileInteractor: SaveUserProfileInteractor,
                             newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): TabContract.Presenter {
        return TabPresenter(getUserProfileInteractor,
                paperRegionsInteractor,
                paperGamesInteractor,
                paperMeetingsInteractor,
                paperPlacesInteractor,
                getAllGamesInteractor,
                modifyUserInteractor,
                saveUserProfileInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesMeetingsPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                  paperMeetingsInteractor: PaperMeetingsInteractor,
                                  getOpenMeetingsInteractor: GetOpenMeetingsInteractor,
                                  getUserMeetingsInteractor: GetUserMeetingsInteractor,
                                  getUserGroupsInteractor: GetUserGroupsInteractor,
                                  getUserPlacesInteractor: GetUserPlacesInteractor,
                                  getGroupMeetingsInteractor: GetGroupMeetingsInteractor,
                                  getPlaceMeetingsInteractor: GetPlaceMeetingsInteractor,
                                  setTopicInteractor: SetTopicInteractor,
                                  clearTopicInteractor: ClearTopicInteractor,
                                  removeMeetingInteractor: RemoveMeetingInteractor,
                                  newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): MeetingsContract.Presenter {
        return MeetingsPresenter(getUserProfileInteractor,
                paperMeetingsInteractor,
                getOpenMeetingsInteractor,
                getUserMeetingsInteractor,
                getUserGroupsInteractor,
                getUserPlacesInteractor,
                getGroupMeetingsInteractor,
                getPlaceMeetingsInteractor,
                setTopicInteractor,
                clearTopicInteractor,
                removeMeetingInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGroupsPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                paperGroupsInteractor: PaperGroupsInteractor,
                                paperMeetingsInteractor: PaperMeetingsInteractor,
                                getUserGroupsInteractor: GetUserGroupsInteractor,
                                getSingleGroupInteractor: GetSingleGroupInteractor,
                                removeGroupInteractor: RemoveGroupInteractor,
                                removeGroupToUserInteractor: RemoveGroupToUserInteractor,
                                SetTopicInteractor: SetTopicInteractor,
                                ClearTopicInteractor: ClearTopicInteractor,
                                newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GroupsContract.Presenter {
        return GroupsPresenter(getUserProfileInteractor,
                paperGroupsInteractor,
                paperMeetingsInteractor,
                getUserGroupsInteractor,
                getSingleGroupInteractor,
                removeGroupInteractor,
                removeGroupToUserInteractor,
                SetTopicInteractor,
                ClearTopicInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesPlacesPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                paperRegionsInteractor: PaperRegionsInteractor,
                                paperPlacesInteractor: PaperPlacesInteractor,
                                getPlacesInteractor: GetPlacesInteractor,
                                removePlaceInteractor: RemovePlaceInteractor,
                                newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): PlacesContract.Presenter {
        return PlacesPresenter(getUserProfileInteractor,
                paperRegionsInteractor,
                paperPlacesInteractor,
                getPlacesInteractor,
                removePlaceInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGamesPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                               paperGamesInteractor: PaperGamesInteractor,
                               getUserGamesInteractor: GetUserGamesInteractor,
                               getGroupGamesInteractor: GetGroupGamesInteractor,
                               getPlaceGamesInteractor: GetPlaceGamesInteractor,
                               getBggXmlGameCollectionInteractor: GetBggXmlGameCollectionInteractor,
                               getBggXmlGameDetailInteractor: GetBggGameDetailInteractor,
                               addGameInteractor: AddGameInteractor,
                               addGameToUserInteractor: AddGameToUserInteractor,
                               addGameToGroupInteractor: AddGameToGroupInteractor,
                               addGameToPlaceInteractor: AddGameToPlaceInteractor,
                               removeGameToUserInteractor: RemoveGameToUserInteractor,
                               removeGameToGroupInteractor: RemoveGameToGroupInteractor,
                               removeGameToPlaceInteractor: RemoveGameToPlaceInteractor,
                               newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GamesContract.Presenter {
        return GamesPresenter(getUserProfileInteractor,
                paperGamesInteractor,
                getUserGamesInteractor,
                getGroupGamesInteractor,
                getPlaceGamesInteractor,
                getBggXmlGameCollectionInteractor,
                getBggXmlGameDetailInteractor,
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
                                       paperMeetingsInteractor: PaperMeetingsInteractor,
                                       getSingleGameInteractor: GetSingleGameInteractor,
                                       getSinglePlaceInteractor: GetSinglePlaceInteractor,
                                       getMeetingUsersInteractor: GetMeetingUsersInteractor,
                                       getSingleUserInteractor: GetSingleUserInteractor,
                                       addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                       modifyMeetingInteractor: ModifyMeetingInteractor,
                                       removeMeetingToUserInteractor: RemoveMeetingToUserInteractor,
                                       setTopicInteractor: SetTopicInteractor,
                                       clearTopicInteractor: ClearTopicInteractor,
                                       firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): MeetingDetailContract.Presenter {
        return MeetingDetailPresenter(getUserProfileInteractor,
                paperMeetingsInteractor,
                getSingleGameInteractor,
                getSinglePlaceInteractor,
                getMeetingUsersInteractor,
                getSingleUserInteractor,
                addMeetingToUserInteractor,
                modifyMeetingInteractor,
                removeMeetingToUserInteractor,
                setTopicInteractor,
                clearTopicInteractor,
                firebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGroupDetailPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                     paperGroupsInteractor: PaperGroupsInteractor,
                                     addGroupToUserInteractor: AddGroupToUserInteractor,
                                     getSingleUserFromMailInteractor: GetSingleUserFromMailInteractor,
                                     getRegionInteractor: GetRegionInteractor,
                                     getGroupUsersInteractor: GetGroupUsersInteractor,
                                     getSingleUserInteractor: GetSingleUserInteractor,
                                     newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GroupDetailContract.Presenter {
        return GroupDetailPresenter(getUserProfileInteractor,
                paperGroupsInteractor,
                addGroupToUserInteractor,
                getSingleUserFromMailInteractor,
                getRegionInteractor,
                getGroupUsersInteractor,
                getSingleUserInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesChatPresenter(profileInteractor: GetUserProfileInteractor,
                              paperGroupsInteractor: PaperGroupsInteractor,
                              getSingleUserInteractor: GetSingleUserInteractor,
                              getGroupUsersInteractor: GetGroupUsersInteractor,
                              getMessagesInteractor: GetMessagesInteractor,
                              sendMessageInteractor: SendMessageInteractor,
                              newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ChatContract.Presenter {
        return ChatPresenter(profileInteractor,
                paperGroupsInteractor,
                getSingleUserInteractor,
                getGroupUsersInteractor,
                getMessagesInteractor,
                sendMessageInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesPlaceDetailPresenter(userProfileInteractor: GetUserProfileInteractor,
                                     paperPlacesInteractor: PaperPlacesInteractor,
                                     paperRegionsInteractor: PaperRegionsInteractor,
                                     firebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): PlaceDetailContract.Presenter {
        return PlaceDetailPresenter(userProfileInteractor,
                paperPlacesInteractor,
                paperRegionsInteractor,
                firebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesGameDetailPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                    paperGamesInteractor: PaperGamesInteractor,
                                    newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): GameDetailContract.Presenter {
        return GameDetailPresenter(getUserProfileInteractor,
                paperGamesInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesAddGroupPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                  paperGroupsInteractor: PaperGroupsInteractor,
                                  getAllUsersInteractor: GetAllUsersInteractor,
                                  getSingleUserFromMailInteractor: GetSingleUserFromMailInteractor,
                                  addGroupInteractor: AddGroupInteractor,
                                  modifyGroupInteractor: ModifyGroupInteractor,
                                  saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                  getPathFromUriInteractor: GetPathFromUriInteractor,
                                  newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddGroupContract.Presenter {
        return AddGroupPresenter(getUserProfileInteractor,
                paperGroupsInteractor,
                getAllUsersInteractor,
                getSingleUserFromMailInteractor,
                addGroupInteractor,
                modifyGroupInteractor,
                saveImageFirebaseStorageInteractor,
                getPathFromUriInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesAddMeetingPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                    paperMeetingsInteractor: PaperMeetingsInteractor,
                                    paperGroupsInteractor: PaperGroupsInteractor,
                                    paperPlacesInteractor: PaperPlacesInteractor,
                                    paperGamesInteractor: PaperGamesInteractor,
                                    addMeetingInteractor: AddMeetingInteractor,
                                    addMeetingToUserInteractor: AddMeetingToUserInteractor,
                                    getUserGamesInteractor: GetUserGamesInteractor,
                                    getGroupGamesInteractor: GetGroupGamesInteractor,
                                    getPlaceGamesInteractor: GetPlaceGamesInteractor,
                                    newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddMeetingContract.Presenter {
        return AddMeetingPresenter(getUserProfileInteractor,
                paperMeetingsInteractor,
                paperGroupsInteractor,
                paperPlacesInteractor,
                paperGamesInteractor,
                addMeetingInteractor,
                addMeetingToUserInteractor,
                getUserGamesInteractor,
                getGroupGamesInteractor,
                getPlaceGamesInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesAddPlacePresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                  paperPlacesInteractor: PaperPlacesInteractor,
                                  paperRegionsInteractor: PaperRegionsInteractor,
                                  getLatLongInteractor: GetLatLongInteractor,
                                  addPlaceInteractor: AddPlaceInteractor,
                                  modifyPlaceInteractor: ModifyPlaceInteractor,
                                  saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                  getPathFromUriInteractor: GetPathFromUriInteractor,
                                  newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): AddPlaceContract.Presenter {
        return AddPlacePresenter(getUserProfileInteractor,
                paperPlacesInteractor,
                paperRegionsInteractor,
                getLatLongInteractor,
                addPlaceInteractor,
                modifyPlaceInteractor,
                saveImageFirebaseStorageInteractor,
                getPathFromUriInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesLoginPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                               loginWithEmailInteractor: LoginWithEmailInteractor,
                               loginWithCredentialsInteractor: LoginWithCredentialsInteractor,
                               paperRegionsInteractor: PaperRegionsInteractor,
                               saveUserProfileInteractor: SaveUserProfileInteractor,
                               getSingleUserInteractor: GetSingleUserInteractor,
                               getAllUsersInteractor: GetAllUsersInteractor,
                               addUserInteractor: AddUserInteractor,
                               modifyUserInteractor: ModifyUserInteractor,
                               getAllRegionInteractor: GetAllRegionInteractor,
                               newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): LoginContract.Presenter {
        return LoginPresenter(getUserProfileInteractor,
                loginWithEmailInteractor,
                loginWithCredentialsInteractor,
                paperRegionsInteractor,
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
    fun providesSignUpPresenter(getUserProfileInteractor: GetUserProfileInteractor,
                                getAllUsersInteractor: GetAllUsersInteractor,
                                paperRegionsInteractor: PaperRegionsInteractor,
                                createMailUserInteractor: CreateMailUserInteractor,
                                addUserInteractor: AddUserInteractor,
                                saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                getPathFromUriInteractor: GetPathFromUriInteractor,
                                newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): SignUpContract.Presenter {
        return SignUpPresenter(getUserProfileInteractor,
                getAllUsersInteractor,
                paperRegionsInteractor,
                createMailUserInteractor,
                addUserInteractor,
                saveImageFirebaseStorageInteractor,
                getPathFromUriInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

    @Provides
    @ActivityScope
    fun providesProfilePresenter(saveImageFirebaseStorageInteractor: SaveImageFirebaseStorageInteractor,
                                 getPathFromUriInteractor: GetPathFromUriInteractor,
                                 saveUserProfileInteractor: SaveUserProfileInteractor,
                                 addUserInteractor: AddUserInteractor,
                                 getUserProfileInteractor: GetUserProfileInteractor,
                                 paperGroupsInteractor: PaperGroupsInteractor,
                                 paperMeetingsInteractor: PaperMeetingsInteractor,
                                 paperPlacesInteractor: PaperPlacesInteractor,
                                 paperRegionsInteractor: PaperRegionsInteractor,
                                 newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): ProfileContract.Presenter {
        return ProfilePresenter(saveImageFirebaseStorageInteractor,
                getPathFromUriInteractor,
                saveUserProfileInteractor,
                addUserInteractor,
                getUserProfileInteractor,
                paperGroupsInteractor,
                paperMeetingsInteractor,
                paperPlacesInteractor,
                paperRegionsInteractor,
                newUseFirebaseAnalyticsInteractor)
    }

}