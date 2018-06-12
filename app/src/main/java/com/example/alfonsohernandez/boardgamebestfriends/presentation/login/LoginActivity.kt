package com.example.alfonsohernandez.boardgamebestfriends.presentation.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import com.facebook.AccessToken
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import javax.inject.Inject
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.signup.SignUpActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity(),
        LoginContract.View,
        View.OnClickListener,
        DialogFactory.CitySelectedCallback{

    private val TAG = "LoginActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCallbackManager: CallbackManager

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    private var provider = ""

    @Inject
    lateinit var presenter: LoginPresenter

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(loginToolbar)
        supportActionBar?.setTitle(getString(R.string.loginToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        presenter.setView(this)

        mAuth = FirebaseAuth.getInstance()

        //FACEBOOK SETTINGS
        mCallbackManager = CallbackManager.Factory.create()
        loginBfacebook.setReadPermissions("email", "public_profile")
        loginBfacebook.registerCallback(mCallbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                result?.let {
                    handleFacebookAccessToken(it.getAccessToken())
                }
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException?) {
                showError(R.string.loginErrorFacebook)
            }
        })

        //GOOGLE SETTINGS
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("241782299389-qre174tkl0gt4g17pjiveho0p2b1nud6.apps.googleusercontent.com")
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        loginBgmail.setOnClickListener(this)
        loginBmail.setOnClickListener(this)
        loginTVsignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.loginBmail ->{
                if(!loginETemail.text.equals("") && !loginETpassword.text.equals(""))
                    presenter.loginWithEmail(loginETemail.text.toString(), loginETpassword.text.toString())
                else
                    showError(R.string.loginErrorEmpty)
            }
            R.id.loginBgmail ->{
                showProgress(true)
                val signInIntent = mGoogleSignInClient.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            R.id.loginTVsignIn ->{
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivityForResult(intent,1)
            }
        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RC_SIGN_IN) {
            showProgress(true)
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess()) {
                result.signInAccount?.let {
                    firebaseAuthWithGoogle(it)
                }
            }
        }else if(requestCode == 1){
            showSuccess(R.string.loginSuccessAdding)
        }else{
            showProgress(true)
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCitySelectedChoosed(city: String) {
        val user = mAuth.currentUser
        if(user != null) {
            if (provider.equals("facebook"))
                presenter.getUsersData(user.uid, User(user.uid, user.email.toString(), user.displayName.toString(), user.photoUrl.toString() + "?type=large", provider, presenter.getRegionId(city)))
            else
                presenter.getUsersData(user.uid, User(user.uid, user.email.toString(), user.displayName.toString(), user.photoUrl.toString(), provider, presenter.getRegionId(city)))
        }
        }

    override fun chooseRegion(fromFacebook: Boolean) {
        DialogFactory.callbackCity = this@LoginActivity
        DialogFactory.buildChooseRegionDialog(this@LoginActivity,presenter.getRegions()).show()
        if(fromFacebook)
            provider = "facebook"
        else
            provider = "gmail"
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        presenter.loginWithCredentials(credential,true)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null)
        presenter.loginWithCredentials(credential,false)
    }

    override fun nextActivity() {
        val main = Intent(this, TabActivity::class.java)
        startActivity(main)
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarLogin?.setVisibility(isLoading)
        loginLLall?.setVisibility(!isLoading)
    }
}
