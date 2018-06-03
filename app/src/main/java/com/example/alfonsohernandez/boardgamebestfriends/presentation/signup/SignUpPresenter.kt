package com.example.alfonsohernandez.boardgamebestfriends.presentation.signup

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseanalytics.NewUseFirebaseAnalyticsInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseregions.GetAllRegionInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.AddUserInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.interactors.firebaseusers.GetAllUsersInteractor
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Created by alfonsohernandez on 06/04/2018.
 */
class SignUpPresenter @Inject constructor(private val getAllUsersInteractor: GetAllUsersInteractor,
                                          private val addUserInteractor: AddUserInteractor,
                                          private val getAllRegionInteractor: GetAllRegionInteractor,
                                          private val newUseFirebaseAnalyticsInteractor: NewUseFirebaseAnalyticsInteractor): SignUpContract.Presenter {

    private val TAG = "SignUpPresenter"

    private var view: SignUpContract.View? = null
    var regionList: ArrayList<Region> = arrayListOf()


    fun setView(view: SignUpContract.View?) {
        this.view = view
        getRegionData()
        firebaseEvent("Showing profile",TAG)
    }

    override fun spinnerItemChange(countrySelected: String) {
        var tempRegionList = regionList
        tempRegionList.filter { it -> it.country.equals(countrySelected) }
        var cityListName = arrayListOf<String>()
        for(region in tempRegionList){
            cityListName.add(region.city)
        }
        view?.setupSpinnerCity(cityListName)
    }

    override fun getUsersData(userMail: String) {
        view?.showProgressBar(true)
        getAllUsersInteractor
                .getFirebaseDataAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var alreadyExist = false
                    for (h in it.children) {
                        if(h.getValue(User::class.java)!!.email.equals(userMail))
                            alreadyExist = true
                    }
                    if(!alreadyExist) {
                        view?.registerUser()
                        firebaseEvent("Signing up", TAG)
                    }else {
                        view?.showErrorUser()
                    }
                },{
                    view?.showProgressBar(false)
                    view?.showErrorAllUsers()
                })
    }

    override fun saveUserData(userId:String, user: User) {
        addUserInteractor
                .addFirebaseDataUser(userId,user)
                .subscribe({
                    view?.finishSignUp()
                },{
                    view?.showErrorUser()
                })
    }

    override fun getRegionIdFromCity(city: String): String {
        var tempRegionList = regionList
        return tempRegionList.filter { it -> it.city.equals(city) }.get(0).id
    }

    override fun getRegionData() {
        view?.showProgressBar(true)
        getAllRegionInteractor
                .getFirebaseDataAllRegions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.showProgressBar(false)
                    var countryListName = arrayListOf<String>()
                    var actualRegion: Region
                    for (h in it.children) {
                        actualRegion = h.getValue(Region::class.java)!!
                        regionList.add(actualRegion)
                        for(region in regionList) {
                            if (!actualRegion.country.equals(region.country) || countryListName.size == 0) {
                                countryListName.add(actualRegion!!.country)
                            }
                        }
                    }
                    regionList = ArrayList(regionList.sortedBy { it.city })
                    view?.setupSpinnerCountry(countryListName)
                    spinnerItemChange(countryListName.get(0))
                },{
                    view?.showProgressBar(false)
                    view?.showErrorRegion()
                })
    }

    override fun getUrlFromPhoto(cursor: Cursor?): String {

        cursor!!.moveToFirst()

        var imagePath = cursor.getString(cursor.getColumnIndex(arrayOf(MediaStore.Images.Media.DATA)[0]))
        var options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        var stream = ByteArrayOutputStream()

        var bitmapRedux = BitmapFactory.decodeFile(imagePath, options)
        bitmapRedux.compress(Bitmap.CompressFormat.PNG, 100, stream)

        var url = Base64.encodeToString(stream.toByteArray(), 0)

        cursor.close()

        return url
    }

    override fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null)
                cursor.close()
        }
    }

    override fun firebaseEvent(id: String, activityName: String) {
        newUseFirebaseAnalyticsInteractor.sendingDataFirebaseAnalytics(id,activityName)
    }
}