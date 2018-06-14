package com.example.alfonsohernandez.boardgamebestfriends.presentation.splash

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseauth.GetCurrentAuthUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetSingleUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class SplashPresenter @Inject constructor(private val getCurrentAuthUserInteractor: GetCurrentAuthUserInteractor,
                                          private val getSingleUserInteractor: GetSingleUserInteractor): SplashContract.Presenter , BasePresenter<SplashContract.View>(){

    private val TAG = "SplashPresenter"

    fun setView(view: SplashContract.View?) {
        this.view = view
        getAuthUser()
    }

    override fun getAuthUser() {
        var user = getCurrentAuthUserInteractor.getFirebaseCurrentAuthUser()
        if(user != null) {
            getUser(user.uid)
        }else {
            view?.startLogin()
        }
    }

    fun getUser(userId: String){
        getSingleUserInteractor
                .getFirebaseDataSingleUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.getValue(User::class.java)!!.regionId.equals(""))
                        view?.startLogin()
                    else
                        view?.startAPP()
                },{
                    view?.showError(R.string.loginErrorLoadingUsers)
                },{
                    view?.startLogin()
                })
    }

    override fun getUserProfile(): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun firebaseEvent(id: String, activityName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}