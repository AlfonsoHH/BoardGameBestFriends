package com.example.alfonsohernandez.boardgamebestfriends.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail.PlaceDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.signup.SignUpActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.mateware.snacky.Snacky
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract.View, View.OnClickListener {

    private val TAG = "ProfileFragment"

    @Inject
    lateinit var presenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

//    override fun showNotification(title: String) {
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()
        presenter.setView(this)

        profileButtonLogout.setOnClickListener(this)
        fragmentProfileIVmyGames.setOnClickListener(this)
        fragmentProfileIVmyMeetings.setOnClickListener(this)
        fragmentProfileIVmyPlace.setOnClickListener(this)
        fragmentProfileIVmodify.setOnClickListener(this)
        fragmentProfileIVphoto.setOnClickListener(this)
    }

    override fun startPlaceDetail(id: String) {
        val intent = Intent(context, PlaceDetailActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("kind", "My Place")
        startActivity(intent)
    }

    fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun startAddMyPlaceDialog() {
        context?.let {ctx ->
            DialogFactory.buildConfirmDialogTitle(ctx, getString(R.string.profileDialogTitle), getString(R.string.profileDialogMessagge), Runnable {
                startAddMyPlace()
            })
        }
    }

    fun startAddMyPlace() {
        val intent = Intent(context, AddPlaceActivity::class.java)
        intent.putExtra("kind", "My Place")
        startActivityForResult(intent, 1)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    override fun setData(userProfile: User?) {
        if(userProfile != null) {
            fragmentProfileTVuserName.text = userProfile.userName
            fragmentProfileTVemail.text = userProfile.email

            if (!userProfile.photo.equals("url")) {
                Glide.with(this)
                        .load(userProfile.photo)
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .into(fragmentProfileIVphoto)
            }
        }
    }

    override fun setRegionData(region: Region) {
        fragmentProfileTVresidence?.text = region.city + ", " + region.country
    }

    fun dialogCameraGallery(){
        val alertDilog = android.support.v7.app.AlertDialog.Builder(context!!).create()
        alertDilog.setMessage(getString(R.string.signUpDialogText))

        alertDilog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.signUpDialogGallery), { dialogInterface, i ->
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, 1)
        })

        alertDilog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.signUpDialogCamera), { dialogInterface, i ->
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 2)
        })

        alertDilog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.signUpDialogUpdate), { dialogInterface, i ->
            val intent = Intent(context, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })
        alertDilog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.profileButtonLogout -> {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                presenter.cleanPaper()
                startLoginActivity()
            }
            R.id.fragmentProfileIVmyGames -> {
                presenter.getUserProfile()?.let {
                    val intent = Intent(context, GamesActivity::class.java)
                    intent.putExtra("kind", "buddy-" + it.id)
                    startActivity(intent)
                }
            }
            R.id.fragmentProfileIVmyMeetings -> {
                presenter.getUserProfile()?.let {
                    val intent = Intent(context, TabActivity::class.java)
                    intent.putExtra("tab", 0)
                    intent.putExtra("kind", "buddy-" + it.id)
                    startActivity(intent)
                }
            }
            R.id.fragmentProfileIVmyPlace-> {
                presenter.myPlaceOnActualRegion()
            }
            R.id.fragmentProfileIVmodify-> {
                context?.let {ctx ->
                    DialogFactory.buildConfirmDialog(ctx, getString(R.string.profileDialogModify), Runnable {
                        fragmentProfileIVphoto.setDrawingCacheEnabled(true)
                        fragmentProfileIVphoto.buildDrawingCache()
                        val bitmap = (fragmentProfileIVphoto.getDrawable() as BitmapDrawable).getBitmap()

                        presenter.saveImage(bitmap)

                        fragmentProfileIVmodify.setVisibility(false)
                    })
                }
            }
            R.id.fragmentProfileIVphoto-> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var camera = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    var needsWrite = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

                    if (!camera && !needsWrite) {
                        dialogCameraGallery()
                    } else {
                        Dexter.withActivity(activity)
                                .withPermissions(Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(object : MultiplePermissionsListener {
                                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                                        Snacky.builder()
                                                .setActivity(activity)
                                                .setActionText(getString(R.string.signUpSnackySettings))
                                                .setActionClickListener(object : View.OnClickListener {
                                                    override fun onClick(v: View?) {
                                                        openPermissionsSettings(activity!!)
                                                    }
                                                })
                                                .setText(getString(R.string.signUpSnackyText))
                                                .setDuration(Snacky.LENGTH_LONG)
                                                .build()
                                                .show()
                                    }

                                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                        camera = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                        needsWrite = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                        if (!camera && !needsWrite) {
                                            dialogCameraGallery()
                                        } else {
                                            Toast.makeText(context, getString(R.string.signUpDialogPermissions), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }).check()
                    }
                }else{
                    dialogCameraGallery()
                }
            }
        }
    }

    fun openPermissionsSettings(activity: FragmentActivity){
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    override fun showError(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarProfile?.setVisibility(isLoading)
        fragmentProfileLLall?.setVisibility(!isLoading)
    }

    override fun setPhotoImage(image: Any) {
        Glide.with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(fragmentProfileIVphoto)
        fragmentProfileIVmodify.setVisibility(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val realPath = presenter.getRealPathFromURI(data.data)
            setPhotoImage(File(realPath))
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            val photo = data.extras.get("data") as Bitmap
            setPhotoImage(photo)
        }
    }
}
