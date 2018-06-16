package com.example.alfonsohernandez.boardgamebestfriends.presentation.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File
import javax.inject.Inject
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePermissionActivity
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BasePermissionActivity(),
        SignUpContract.View,
        View.OnClickListener,
        BasePermissionActivity.PermissionCallback {

    private val TAG = "AddMeetingActivity"

    private var url: String = "url"

    @Inject
    lateinit var presenter: SignUpPresenter

    lateinit var spinnerArrayAdapterCountry: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterCity: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(signUpToolbar)
        supportActionBar?.setTitle(getString(R.string.signUpToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        presenter.setView(this)

        super.callbackPermissions = this

        signUpButton.setOnClickListener(this)
        signUpIVprofilePicture.setOnClickListener(this)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    override fun onImageReceived(intent: Intent, fromGallery: Boolean) {
        if(fromGallery)
            setPhotoImage(File(presenter.getRealPathFromURI(intent.data)))
        else
            setPhotoImage(intent.extras.get("data") as Bitmap)
    }

    override fun setPhotoImage(image: Any) {
        Glide.with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(signUpIVprofilePicture)
    }

    override fun saveUser(user: FirebaseUser) {
        signUpIVprofilePicture.setDrawingCacheEnabled(true)
        signUpIVprofilePicture.buildDrawingCache()
        val bitmap = (signUpIVprofilePicture.getDrawable() as BitmapDrawable).getBitmap()

        presenter.saveImage(user.uid,User(user.uid,signUpTVemail.text.toString(),signUpTVuserName.text.toString(),url,"email",presenter.getRegionIdFromCity(signUpScity.selectedItem.toString())),bitmap)

        FirebaseAuth.getInstance().signOut()
        finishSignUp()
    }

    override fun finishSignUp() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun setupSpinnerCountry(countryList: ArrayList<String>) {
        spinnerArrayAdapterCountry = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
        spinnerArrayAdapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        signUpScountry.setAdapter(spinnerArrayAdapterCountry)

        signUpScountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                presenter.spinnerItemChange(signUpScountry.selectedItem.toString())
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    override fun setupSpinnerCity(cityList: ArrayList<String>) {
        spinnerArrayAdapterCity = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
        spinnerArrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        signUpScity.setAdapter(spinnerArrayAdapterCity)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signUpButton ->{
                if(!signUpTVuserName.text.toString().equals("") && !signUpTVemail.text.toString().equals("") && !signUpTVpassword.text.toString().equals("") && !signUpTVpasswordConfirm.text.toString().equals("")) {
                    if (signUpTVpassword.text.toString().equals(signUpTVpasswordConfirm.text.toString()))
                        if(signUpTVpassword.text.length > 5)
                            presenter.getUsersData(signUpTVemail.text.toString(),signUpTVpassword.text.toString())
                        else
                            showError(R.string.signUpErrorPasswordLenght)
                    else
                        showError(R.string.signUpErrorPassword)
                }else{
                    showError(R.string.signUpErrorEmpty)
                }
            }
            R.id.signUpIVprofilePicture ->{
                askForPermissionsPhoto(this@SignUpActivity)
            }
        }
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarSignUp?.setVisibility(isLoading)
        signUpLLall?.setVisibility(!isLoading)
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
