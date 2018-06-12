package com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules

import android.content.Context
import com.example.alfonsohernandez.boardgamebestfriends.domain.contentresolver.GetPathFromUri
import com.example.alfonsohernandez.boardgamebestfriends.domain.geocoder.Geocoder
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.GetMessagesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.GetMessagesInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.SendMessageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasechat.SendMessageInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegames.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasegroups.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasemeetings.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebasestorage.SaveImageFirebaseStorageInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.*
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggGameCollectionInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggXmlGameCollectionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamecollection.GetBggXmlGameCollectionInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail.GetBggGameDetailInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getbgggamedetail.GetBggGameDetailInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getlatlong.GetLatLongInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getlatlong.GetLatLongInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.getpathfromuri.GetPathFromUriInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergames.PaperGamesInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papergroups.PaperGroupsInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.papermeetings.PaperMeetingsInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperplaces.PaperPlacesInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.paperregions.PaperRegionsInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.ClearTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.ClearTopicInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.SetTopicInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.topic.SetTopicInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.SaveUserProfileInteractorImpl
import com.example.alfonsohernandez.boardgamebestfriends.domain.repository.*
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggApi
import com.example.alfonsohernandez.boardgamebestfriends.network.rest.BggXMLapi
import com.example.alfonsohernandez.boardgamebestfriends.push.FCMtopic
import com.example.alfonsohernandez.boardgamebestfriends.storage.preferences.UserProfileDatabase
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(DatabaseModule::class,ApiModule::class))
class InteractorModule {

    @Provides
    fun providesNewUseFirebaseAnalyticsInteractorImpl(context: Context): NewUseFirebaseAnalyticsInteractor {
        return NewUseFirebaseAnalyticsInteractorImpl(context)
    }

    @Provides
    fun providesGetMessagesInteractorImpl(chatRepository: ChatRepository): GetMessagesInteractor {
        return GetMessagesInteractorImpl(chatRepository)
    }

    @Provides
    fun providesSendMessageInteractorImpl(chatRepository: ChatRepository): SendMessageInteractor {
        return SendMessageInteractorImpl(chatRepository)
    }

    @Provides
    fun providesAddGameInteractorImpl(gamesRepository: GamesRepository): AddGameInteractor {
        return AddGameInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesAddGameToGroupInteractorImpl(gamesRepository: GamesRepository): AddGameToGroupInteractor {
        return AddGameToGroupInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesAddGameToPlaceInteractorImpl(gamesRepository: GamesRepository): AddGameToPlaceInteractor {
        return AddGameToPlaceInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesAddGameToUserInteractorImpl(gamesRepository: GamesRepository): AddGameToUserInteractor {
        return AddGameToUserInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesGetGroupGamesInteractorImpl(gamesRepository: GamesRepository): GetGroupGamesInteractor {
        return GetGroupGamesInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesGetPlaceGamesInteractorImpl(gamesRepository: GamesRepository): GetPlaceGamesInteractor {
        return GetPlaceGamesInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesGetSingleGameInteractorImpl(gamesRepository: GamesRepository): GetSingleGameInteractor {
        return GetSingleGameInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesGetUserGamesInteractorImpl(gamesRepository: GamesRepository): GetUserGamesInteractor {
        return GetUserGamesInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesRemoveGameToGroupInteractorImpl(gamesRepository: GamesRepository): RemoveGameToGroupInteractor {
        return RemoveGameToGroupInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesRemoveGameToPlaceInteractorImpl(gamesRepository: GamesRepository): RemoveGameToPlaceInteractor {
        return RemoveGameToPlaceInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesRemoveGameToUserInteractorImpl(gamesRepository: GamesRepository): RemoveGameToUserInteractor {
        return RemoveGameToUserInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesAddGroupInteractorImpl(groupsRepository: GroupsRepository): AddGroupInteractor {
        return AddGroupInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesAddGroupToUserInteractorImpl(groupsRepository: GroupsRepository): AddGroupToUserInteractor {
        return AddGroupToUserInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesGetSingleGroupInteractorImpl(groupsRepository: GroupsRepository): GetSingleGroupInteractor {
        return GetSingleGroupInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesGetUserGroupsInteractorImpl(groupsRepository: GroupsRepository): GetUserGroupsInteractor {
        return GetUserGroupsInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesModifyGroupInteractorImpl(groupsRepository: GroupsRepository): ModifyGroupInteractor {
        return ModifyGroupInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesRemoveGroupInteractorImpl(groupsRepository: GroupsRepository): RemoveGroupInteractor {
        return RemoveGroupInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesRemoveGroupToUserInteractorImpl(groupsRepository: GroupsRepository): RemoveGroupToUserInteractor {
        return RemoveGroupToUserInteractorImpl(groupsRepository)
    }

    @Provides
    fun providesAddMeetingInteractorImpl(meetingsRepository: MeetingsRepository): AddMeetingInteractor {
        return AddMeetingInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesAddMeetingToUserInteractorImpl(meetingsRepository: MeetingsRepository): AddMeetingToUserInteractor {
        return AddMeetingToUserInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesAddMeetingToPlaceInteractorImpl(meetingsRepository: MeetingsRepository): AddMeetingToPlaceInteractor {
        return AddMeetingToPlaceInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesGetOpenMeetingsInteractorImpl(meetingsRepository: MeetingsRepository): GetOpenMeetingsInteractor {
        return GetOpenMeetingsInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesGetSingleMeetingInteractorImpl(meetingsRepository: MeetingsRepository): GetSingleMeetingInteractor {
        return GetSingleMeetingInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesGetUserMeetingsInteractorImpl(meetingsRepository: MeetingsRepository): GetUserMeetingsInteractor {
        return GetUserMeetingsInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesGetGroupMeetingsInteractorImpl(meetingsRepository: MeetingsRepository): GetGroupMeetingsInteractor {
        return GetGroupMeetingsInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesGetPlaceMeetingsInteractorImpl(meetingsRepository: MeetingsRepository): GetPlaceMeetingsInteractor {
        return GetPlaceMeetingsInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesModifyMeetingInteractorImpl(meetingsRepository: MeetingsRepository): ModifyMeetingInteractor {
        return ModifyMeetingInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesRemoveMeetingInteractorImpl(meetingsRepository: MeetingsRepository): RemoveMeetingInteractor {
        return RemoveMeetingInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesRemoveMeetingToUserInteractorImpl(meetingsRepository: MeetingsRepository): RemoveMeetingToUserInteractor {
        return RemoveMeetingToUserInteractorImpl(meetingsRepository)
    }

    @Provides
    fun providesAddPlaceInteractorImpl(placesRepository: PlacesRepository): AddPlaceInteractor {
        return AddPlaceInteractorImpl(placesRepository)
    }

    @Provides
    fun providesGetOpenPlacesInteractorImpl(placesRepository: PlacesRepository): GetPlacesInteractor {
        return GetPlacesInteractorImpl(placesRepository)
    }

    @Provides
    fun providesGetUserPlacesInteractorImpl(placesRepository: PlacesRepository): GetUserPlacesInteractor {
        return GetUserPlacesInteractorImpl(placesRepository)
    }

    @Provides
    fun providesGetSinglePlaceInteractorImpl(placesRepository: PlacesRepository): GetSinglePlaceInteractor {
        return GetSinglePlaceInteractorImpl(placesRepository)
    }

    @Provides
    fun providesModifyPlaceInteractorImpl(placesRepository: PlacesRepository): ModifyPlaceInteractor {
        return ModifyPlaceInteractorImpl(placesRepository)
    }

    @Provides
    fun providesRemovePlaceInteractorImpl(placesRepository: PlacesRepository): RemovePlaceInteractor {
        return RemovePlaceInteractorImpl(placesRepository)
    }

    @Provides
    fun providesAddUserInteractorImpl(usersRepository: UsersRepository): AddUserInteractor {
        return AddUserInteractorImpl(usersRepository)
    }

    @Provides
    fun providesGetAllUsersInteractorImpl(usersRepository: UsersRepository): GetAllUsersInteractor {
        return GetAllUsersInteractorImpl(usersRepository)
    }

    @Provides
    fun providesGetGroupUsersInteractorImpl(usersRepository: UsersRepository): GetGroupUsersInteractor {
        return GetGroupUsersInteractorImpl(usersRepository)
    }

    @Provides
    fun providesGetMeetingUsersInteractorImpl(usersRepository: UsersRepository): GetMeetingUsersInteractor {
        return GetMeetingUsersInteractorImpl(usersRepository)
    }

    @Provides
    fun providesGetSingleUserInteractorImpl(usersRepository: UsersRepository): GetSingleUserInteractor {
        return GetSingleUserInteractorImpl(usersRepository)
    }

    @Provides
    fun providesGetSingleUserFromMailInteractorImpl(usersRepository: UsersRepository): GetSingleUserFromMailInteractor {
        return GetSingleUserFromMailInteractorImpl(usersRepository)
    }

    @Provides
    fun providesModifyUserInteractorImpl(usersRepository: UsersRepository): ModifyUserInteractor {
        return ModifyUserInteractorImpl(usersRepository)
    }

    @Provides
    fun providesGetUserProfileInteractorImpl(database: UserProfileDatabase): GetUserProfileInteractor {
        return GetUserProfileInteractorImpl(database)
    }

    @Provides
    fun providesSaveUserProfileInteractorImpl(database: UserProfileDatabase): SaveUserProfileInteractor {
        return SaveUserProfileInteractorImpl(database)
    }

    @Provides
    fun providesGetAllRegionInteractorImpl(regionsRepository: RegionsRepository): GetAllRegionInteractor {
        return GetAllRegionInteractorImpl(regionsRepository)
    }

    @Provides
    fun providesGetRegionInteractorImpl(regionsRepository: RegionsRepository): GetRegionInteractor {
        return GetRegionInteractorImpl(regionsRepository)
    }

    @Provides
    fun providesGetAllGamesInteractorImpl(gamesRepository: GamesRepository): GetAllGamesInteractor {
        return GetAllGamesInteractorImpl(gamesRepository)
    }

    @Provides
    fun providesGetBggGameCollectionInteractorImpl(bggApi: BggApi): GetBggGameCollectionInteractor {
        return GetBggGameCollectionInteractorImpl(bggApi)
    }

    @Provides
    fun providesSetTopicInteractorImpl(fcMtopic: FCMtopic): SetTopicInteractor {
        return SetTopicInteractorImpl(fcMtopic)
    }

    @Provides
    fun providesClearTopicInteractorImpl(fcMtopic: FCMtopic): ClearTopicInteractor {
        return ClearTopicInteractorImpl(fcMtopic)
    }

    @Provides
    fun providesGetLatLongInteractorImpl(geocoder: Geocoder): GetLatLongInteractor {
        return GetLatLongInteractorImpl(geocoder)
    }

    @Provides
    fun providesGetPathFromUriInteractorImpl(getPathFromUri: GetPathFromUri): GetPathFromUriInteractor {
        return GetPathFromUriInteractorImpl(getPathFromUri)
    }

    @Provides
    fun providesPaperGroupsInteractorImpl(paperGroupsRepository: PaperGroupsRepository): PaperGroupsInteractor {
        return PaperGroupsInteractorImpl(paperGroupsRepository)
    }

    @Provides
    fun providesPaperMeetingsInteractorImpl(paperMeetingsRepository: PaperMeetingsRepository): PaperMeetingsInteractor {
        return PaperMeetingsInteractorImpl(paperMeetingsRepository)
    }

    @Provides
    fun providesPaperPlacesInteractorImpl(paperPlacesRepository: PaperPlacesRepository): PaperPlacesInteractor {
        return PaperPlacesInteractorImpl(paperPlacesRepository)
    }

    @Provides
    fun providesPaperRegionsInteractorImpl(paperRegionsRepository: PaperRegionsRepository): PaperRegionsInteractor {
        return PaperRegionsInteractorImpl(paperRegionsRepository)
    }

    @Provides
    fun providesPaperGamesInteractorImpl(paperGamesRepository: PaperGamesRepository): PaperGamesInteractor {
        return PaperGamesInteractorImpl(paperGamesRepository)
    }

    @Provides
    fun providesGetBggGameDetailInteractorImpl(bggXMLapi: BggXMLapi): GetBggGameDetailInteractor {
        return GetBggGameDetailInteractorImpl(bggXMLapi)
    }

    @Provides
    fun providesGetBggXmlGameCollectionInteractorImpl(bggXMLapi: BggXMLapi): GetBggXmlGameCollectionInteractor {
        return GetBggXmlGameCollectionInteractorImpl(bggXMLapi)
    }

    @Provides
    fun providesSaveImageFirebaseStorageInteractorImpl(imageRepository: ImageRepository): SaveImageFirebaseStorageInteractor {
        return SaveImageFirebaseStorageInteractorImpl(imageRepository)
    }

    @Provides
    fun providesCreateMailUserInteractorImpl(authRepository: AuthRepository): CreateMailUserInteractor {
        return CreateMailUserInteractorImpl(authRepository)
    }

    @Provides
    fun providesLoginWithCredentialsInteractorImpl(authRepository: AuthRepository): LoginWithCredentialsInteractor {
        return LoginWithCredentialsInteractorImpl(authRepository)
    }

    @Provides
    fun providesLoginWithEmailInteractorImpl(authRepository: AuthRepository): LoginWithEmailInteractor {
        return LoginWithEmailInteractorImpl(authRepository)
    }

}