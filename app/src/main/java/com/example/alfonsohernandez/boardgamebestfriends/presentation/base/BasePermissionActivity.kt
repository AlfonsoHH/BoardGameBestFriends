package com.example.alfonsohernandez.boardgamebestfriends.presentation.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.mateware.snacky.Snacky
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File

abstract class BasePermissionActivity: BaseNotificationActivity() {

    fun askForPermissionLocation(activity: Activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            var locationFine = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            var locationCoarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

            if (locationFine || locationCoarse) {
                Dexter.withActivity(activity)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                                Snacky.builder()
                                        .setActivity(activity)
                                        .setActionText(getString(R.string.tabSnackySettings))
                                        .setActionClickListener(object : View.OnClickListener {
                                            override fun onClick(v: View?) {
                                                openPermissionsSettings(activity)
                                            }
                                        })
                                        .setText(getString(R.string.tabSnackyText))
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .build()
                                        .show()
                            }

                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                locationFine = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                locationCoarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                if (locationFine || locationCoarse) {
                                    Toast.makeText(activity, getString(R.string.tabPermissions), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }).check()
            }

        }
    }

    fun askForPermissionsPhoto(activity: Activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            var camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            var needsWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

            if (!camera && !needsWrite) {
                dialogCameraGallery(activity)
            } else {
                Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                                Snacky.builder()
                                        .setActivity(activity)
                                        .setActionText(getString(R.string.signUpSnackySettings))
                                        .setActionClickListener(object : View.OnClickListener {
                                            override fun onClick(v: View?) {
                                                openPermissionsSettings(activity)
                                            }
                                        })
                                        .setText(getString(R.string.signUpSnackyText))
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .build()
                                        .show()
                            }

                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                needsWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                if (!camera && !needsWrite) {
                                    dialogCameraGallery(activity)
                                } else {
                                    Toast.makeText(activity, getString(R.string.signUpDialogPermissions), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }).check()
            }

        }else{
            dialogCameraGallery(activity)
        }

    }

    fun dialogCameraGallery(activity: Activity){
        val alertDilog = android.support.v7.app.AlertDialog.Builder(activity).create()
        alertDilog.setMessage(getString(R.string.signUpDialogText))

        alertDilog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.signUpDialogGallery), { dialogInterface, i ->
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, 1)
        })

        alertDilog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, getString(R.string.signUpDialogCamera), { dialogInterface, i ->
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 2)
        })

        alertDilog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.signUpDialogUpdate), { dialogInterface, i ->
            val intent = Intent(activity, activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })
        alertDilog.show()
    }

    fun openPermissionsSettings(activity: Activity){
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    var callbackPermissions: PermissionCallback? = null

    interface PermissionCallback {
        fun onImageReceived(intent: Intent, fromGallery: Boolean)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            callbackPermissions?.onImageReceived(data,true)
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            callbackPermissions?.onImageReceived(data,false)
        }
    }
}