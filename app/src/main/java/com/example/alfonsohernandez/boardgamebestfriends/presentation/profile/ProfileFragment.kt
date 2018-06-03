package com.example.alfonsohernandez.boardgamebestfriends.presentation.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting.AddMeetingActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail.PlaceDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract.View {

    private val TAG = "ProfileFragment"

    @Inject
    lateinit var presenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()
        presenter.setView(this)

        profileButtonLogout.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            startLoginActivity()
        })

        fragmentProfileIVmyGames.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(context, GamesActivity::class.java)
                intent.putExtra("kind", "buddy-" + presenter.getUserProfile()!!.id)
                startActivity(intent)
            }
        })

        fragmentProfileIVmyMeetings.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(context, TabActivity::class.java)
                intent.putExtra("tab",0)
                intent.putExtra("kind", "buddy-" + presenter.getUserProfile()!!.id)
                startActivity(intent)
            }
        })

        fragmentProfileIVmyPlace.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                presenter.myPlaceOnActualRegion()
            }
        })
    }

    override fun startPlaceDetail(id: String) {
        val intent = Intent(context, PlaceDetailActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("kind","My Place")
        startActivity(intent)
    }

    fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun startAddMyPlaceDialog() {
        val alertDilog = AlertDialog.Builder(context!!).create()
        alertDilog.setTitle(getString(R.string.profileDialogTitle))
        alertDilog.setMessage(getString(R.string.profileDialogMessagge))

        alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.profileDialogYes), { dialogInterface, i ->
            startAddMyPlace()
        })

        alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.profileDialogNo), { dialogInterface, i ->
            alertDilog.dismiss()
        })
        alertDilog.show()
    }

    fun startAddMyPlace(){
        val intent = Intent(context, AddPlaceActivity::class.java)
        intent.putExtra("kind","My Place")
        startActivityForResult(intent,1)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    override fun setData(userProfile: User?) {
        fragmentProfileTVuserName.text = userProfile!!.userName
        fragmentProfileTVemail.text = userProfile!!.email

        if(userProfile.service.equals("email")){
            if(!userProfile.photo.equals("url")) {
                val decodedString = Base64.decode(userProfile.photo, Base64.DEFAULT)
                val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                Bitmap.createScaledBitmap(imagenJug, 90, 90, false)
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
                roundedBitmapDrawable.isCircular = true

                fragmentProfileIVphoto.setImageDrawable(roundedBitmapDrawable)
            }
        }else{
            Glide.with(this).asBitmap().load(userProfile.photo).into(object : SimpleTarget<Bitmap>(180,180) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val roundedDrawable = RoundedBitmapDrawableFactory.create(context!!.resources, resource)
                    roundedDrawable.isCircular = true
                    fragmentProfileIVphoto.setImageDrawable(roundedDrawable)
                }
            })
        }
    }

    override fun setRegionData(region: Region) {
        fragmentProfileTVresidence.text = region.city + ", " + region.country
    }

    override fun showErrorLogout() {
        Toast.makeText(context, getString(R.string.profileErrorLogout), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRegion() {
        Toast.makeText(context, getString(R.string.profileErrorRegion), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorPlaces() {
        Toast.makeText(context, getString(R.string.profileErrorPlaces), Toast.LENGTH_SHORT).show()
    }

    override fun successAddingMyPlace() {
        Toast.makeText(context, getString(R.string.profileSuccessAdding), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarProfile.setVisibility(boolean)
        fragmentProfileLLall.setVisibility(!boolean)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
            successAddingMyPlace()
    }
}
