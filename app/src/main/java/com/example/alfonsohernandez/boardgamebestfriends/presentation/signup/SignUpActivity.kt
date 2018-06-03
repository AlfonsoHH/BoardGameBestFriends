package com.example.alfonsohernandez.boardgamebestfriends.presentation.signup

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_add_group.*
import kotlinx.android.synthetic.main.activity_add_meeting.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import javax.inject.Inject


class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    private val TAG = "AddMeetingActivity"

    private var url: String = "url"

    @Inject
    lateinit var presenter: SignUpPresenter

    lateinit var spinnerArrayAdapterCountry: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterCity: ArrayAdapter<String>

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(signUpToolbar)
        supportActionBar!!.setTitle(getString(R.string.signUpToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        presenter.setView(this)

        mAuth = FirebaseAuth.getInstance()

        signUpButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(!signUpTVuserName.text.toString().equals("") && !signUpTVemail.text.toString().equals("") && !signUpTVpassword.text.toString().equals("") && !signUpTVpasswordConfirm.text.toString().equals(""))
                if(signUpTVpassword.text.toString().equals(signUpTVpasswordConfirm.text.toString()))
                    presenter.getUsersData(signUpTVemail.text.toString())
                else
                    showErrorPassword()
            }
        })

        signUpIVprofilePicture.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, 1)
            }
        })
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            val pickedImage = data.data
            val roundedDrawable = RoundedBitmapDrawableFactory.create(applicationContext.resources, BitmapFactory.decodeFile(presenter.getRealPathFromURI(applicationContext, pickedImage)))
            roundedDrawable.isCircular = true
            addGroupIVphoto.setImageDrawable(roundedDrawable)
            addGroupIVphoto.adjustViewBounds = true

            url = presenter.getUrlFromPhoto(contentResolver.query(pickedImage!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null))

        }
    }

    override fun registerUser(){
        mAuth.createUserWithEmailAndPassword(signUpTVemail.text.toString(), signUpTVpassword.text.toString())
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            var user = mAuth.currentUser
                            presenter.saveUserData(user!!.uid,User(user!!.uid,signUpTVemail.text.toString(),signUpTVuserName.text.toString(),url,"email",presenter.getRegionIdFromCity(signUpScity.selectedItem.toString())))
                            finishSignUp()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException())
                            Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
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

    override fun showErrorRegister() {
        Toast.makeText(this, getString(R.string.signUpErrorRegister), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmail() {
        Toast.makeText(this, getString(R.string.signUpErrorEmail), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorPassword() {
        Toast.makeText(this, getString(R.string.signUpErrorPassword), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorUser() {
        Toast.makeText(this, getString(R.string.signUpErrorUser), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRegion() {
        Toast.makeText(this, getString(R.string.signUpErrorRegion), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAllUsers() {
        Toast.makeText(this, getString(R.string.signUpErrorAllUsers), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmpty() {
        Toast.makeText(this, getString(R.string.signUpErrorEmpty), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarSignUp.setVisibility(boolean)
        signUpLLall.setVisibility(!boolean)
    }
}
