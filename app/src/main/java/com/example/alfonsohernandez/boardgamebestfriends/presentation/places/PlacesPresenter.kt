package com.example.alfonsohernandez.boardgamebestfriends.presentation.places

import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseplaces.GetOpenPlacesInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.usermanager.GetUserProfileInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class PlacesPresenter @Inject constructor(private val getUserProfileInteractor: GetUserProfileInteractor,
                                          private val getOpenPlacesInteractor: GetOpenPlacesInteractor): PlacesContract.Presenter {

    private var view: PlacesContract.View? = null

    private val TAG = "PlacesPresenter"

    fun setView(view: PlacesContract.View?) {
        this.view = view
    }

    override fun getUserProfile(): User? {
        return getUserProfileInteractor.getProfile()
    }

    override fun getPlacesData() {
        view?.showProgress(true)
        getOpenPlacesInteractor
                .getFirebaseDataOpenPlaces(getUserProfile()!!.regionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgress(false)
                    var placesList = arrayListOf<Place>()
                    for (h in it.children) {
                        var place = h.getValue(Place::class.java)
                        if(place!!.openPlace)
                            placesList.add(place!!)
                    }
                    view?.setData(placesList)
                },{
                    view?.showProgress(false)
                    view?.showErrorPlaces()
                })
    }
}