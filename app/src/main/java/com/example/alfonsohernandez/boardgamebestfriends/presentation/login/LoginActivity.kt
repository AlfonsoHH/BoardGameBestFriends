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
import com.example.alfonsohernandez.boardgamebestfriends.presentation.signup.SignUpActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private val TAG = "LoginActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCallbackManager: CallbackManager

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    lateinit var spinnerArrayAdapterCountry: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterCity: ArrayAdapter<String>

    @Inject
    lateinit var presenter: LoginPresenter

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(loginToolbar)
        supportActionBar!!.setTitle(getString(R.string.loginToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        //AQUI PEDIMOS LOS PERMISOS

        val needsRead = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        val location = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        val locationCoarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

        val internet = ActivityCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED

        val camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (needsRead || location || camera || locationCoarse || internet) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 2909)
            }
        }

        injectDependencies()
        presenter.setView(this)

        mAuth = FirebaseAuth.getInstance()

        //FACEBOOK
        mCallbackManager = CallbackManager.Factory.create()
        loginBfacebook.setReadPermissions("email", "public_profile");
        loginBfacebook.registerCallback(mCallbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.getAccessToken());
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException?) {
                showErrorFacebook()
            }
        })

        //GOOGLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("241782299389-qre174tkl0gt4g17pjiveho0p2b1nud6.apps.googleusercontent.com")
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        loginBgmail.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val signInIntent = mGoogleSignInClient.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        })

        //MAIL AND PASSWORD
        loginBmail.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                mAuth.signInWithEmailAndPassword(loginETemail.text.toString(), loginETpassword.text.toString())
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful) {
                                presenter.getSingleUser(mAuth.currentUser!!.uid)
                            } else {
                                showErrorEmail()
                            }
                        }
            }
        })

        //SIGN UP
        loginTVsignIn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivityForResult(intent,1)
            }
        })

    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess()) {
                firebaseAuthWithGoogle(result.getSignInAccount()!!)
            }
        }else if(requestCode == 1){
            successAdding()
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            chooseRegion("facebook")
                        } else {
                            showErrorFacebook()
                        }
                    }
                })
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            chooseRegion("gmail")
                        } else {
                            showErrorGmail()
                        }
                    }
                })
    }

    override fun nextActivity() {
        val main = Intent(this, TabActivity::class.java)
        startActivity(main)
    }

    override fun showErrorChecking() {
        Toast.makeText(this, getString(R.string.loginErrorCheckingEmail), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmail() {
        Toast.makeText(this, getString(R.string.loginErrorEmail), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorFacebook() {
        Toast.makeText(this, getString(R.string.loginErrorFacebook), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorGmail() {
        Toast.makeText(this, getString(R.string.loginErrorGmail), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorSavingUser() {
        Toast.makeText(this, getString(R.string.loginErrorSavingUser), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorLoadingUsers() {
        Toast.makeText(this, getString(R.string.loginErrorUsers), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorLoadingRegions() {
        Toast.makeText(this, getString(R.string.loginErrorLoadingRegion), Toast.LENGTH_SHORT).show()
    }

    override fun successAdding() {
        Toast.makeText(this, getString(R.string.loginSuccessAdding), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(isLoading: Boolean) {
        progressBarLogin.setVisibility(isLoading)
        loginLLall.setVisibility(!isLoading)
    }

    fun chooseRegion(provider: String){
        var builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.dialog_region,null)
        builder.setView(view)

        var spinnerCountry: Spinner = view.findViewById(R.id.dialogRegionScountry)
        var spinnerCity: Spinner = view.findViewById(R.id.dialogRegionScity)

        spinnerArrayAdapterCountry = ArrayAdapter(this, android.R.layout.simple_spinner_item, presenter.getCountryList())
        spinnerArrayAdapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.setAdapter(spinnerArrayAdapterCountry)

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerArrayAdapterCity.clear()
                spinnerArrayAdapterCity.addAll(presenter.getCityList(spinnerCountry.selectedItem.toString()))
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        spinnerArrayAdapterCity = ArrayAdapter(this, android.R.layout.simple_spinner_item, presenter.getCityList(spinnerCountry.selectedItem.toString()))
        spinnerArrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.setAdapter(spinnerArrayAdapterCity)

        builder.setTitle(getString(R.string.loginRegionDialogTitle))
        builder.setPositiveButton(getString(R.string.loginDialogPositive),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                var user = mAuth.currentUser
                if(provider.equals("facebook"))
                    presenter.getUsersData(user!!.uid,User(user!!.uid,user!!.email.toString(),user!!.displayName.toString(),user!!.photoUrl.toString() + "?type=large",provider,presenter.getRegionId(spinnerCity.selectedItem.toString())))
                else
                    presenter.getUsersData(user!!.uid,User(user!!.uid,user!!.email.toString(),user!!.displayName.toString(),user!!.photoUrl.toString(),provider,presenter.getRegionId(spinnerCity.selectedItem.toString())))
            }
        })
        builder.setNegativeButton(getString(R.string.loginDialogNegative),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
            }
        })
        var dialog: Dialog = builder.create()
        dialog.show()
    }
}
