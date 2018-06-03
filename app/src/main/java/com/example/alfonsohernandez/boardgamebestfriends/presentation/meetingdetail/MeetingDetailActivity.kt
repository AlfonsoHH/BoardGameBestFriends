package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterMembers
import kotlinx.android.synthetic.main.activity_meeting_detail.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import java.util.Calendar.NARROW_STANDALONE



class MeetingDetailActivity : AppCompatActivity(), MeetingDetailContract.View {

    private val TAG = "MeetingDetailActivity"
    var meetingId: String = ""
    var owner: Boolean = false

    var adapter = AdapterMembers()

    @Inject
    lateinit var presenter: MeetingDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_detail)

        setSupportActionBar(meetingDetailToolbar)
        supportActionBar!!.setTitle(getString(R.string.meetingDetailToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if(savedInstanceState!=null)
            meetingId = savedInstanceState.getString("id","")
        else{
            var intent = intent.extras
            meetingId = intent.getString("id","")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this,meetingId)

        meetingDetailTVjoin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(meetingDetailTVjoin.text.toString().equals(getString(R.string.meetingDetailJoin))) {
                    presenter.joinGame(true)
                    var vacant = meetingDetailTVvacants.text.substring(0,1).toInt() - 1
                    meetingDetailTVvacants.text = vacant.toString() + meetingDetailTVvacants.text.substring(1,meetingDetailTVvacants.text.length)
                    meetingDetailTVjoin.text = getString(R.string.meetingDetailLeave)
                }else {
                    presenter.joinGame(false)
                    var vacant = meetingDetailTVvacants.text.substring(0,1).toInt() + 1
                    meetingDetailTVvacants.text = vacant.toString() + meetingDetailTVvacants.text.substring(1,meetingDetailTVvacants.text.length)
                    meetingDetailTVjoin.text = getString(R.string.meetingDetailJoin)
                }
            }
        })
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null,"")
        super.onDestroy()
    }

    override fun setMeetingData(meeting: Meeting) {
        meetingDetailTVmeetingDetail.text = meeting.title
        meetingDetailTVmeetingDescription.text = meeting.description
        meetingDetailTVhour.text = meeting.date.substring(0,meeting.date.lastIndexOf("_"))
        meetingDetailTVdate.text = meeting.date.substring(meeting.date.lastIndexOf("_")+1,meeting.date.length-1)
        meetingDetailTVvacants.text = meeting.vacants.toString() + getString(R.string.meetingDetailVacants)

        meetingDetailTVjoin.text = getString(R.string.meetingDetailJoin)
    }

    override fun showErrorJoining() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorJoiningMeeting), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorLeaving() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorLevingMeeting), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMeeting() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorMeeting), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorPlace() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorGame() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorGame), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMembers() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorMembers), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorVacant() {
        Toast.makeText(this, getString(R.string.meetingDetailErrorVacant), Toast.LENGTH_SHORT).show()
    }

    override fun successJoining() {
        Toast.makeText(this, getString(R.string.meetingDetailSuccessJoining), Toast.LENGTH_SHORT).show()
    }

    override fun successLeaving() {
        Toast.makeText(this, getString(R.string.meetingDetailSuccessLeaving), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarMeetingDetail.setVisibility(boolean)
        meetingDetailFLall.setVisibility(!boolean)
    }

    override fun setupRecycler() {
        meetingDetailRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        meetingDetailRV.adapter = adapter
    }

    override fun setGameData(game: Game) {
        Glide.with(this).asBitmap().load(game.photo).into(object : SimpleTarget<Bitmap>(120,120) {
            override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                roundedDrawable.isCircular = true
                meetingDetailIVgame.setImageDrawable(roundedDrawable)
            }
        })
        meetingDetailTVgameTitle.text = game.title
        meetingDetailRatingBar.rating = game.rating.toFloat()
        meetingDetailTVplayers.text = game.minPlayers.toString() + " - " + game.maxPlayers.toString()
        meetingDetailTVduration.text = game.playingTime.toString()
    }

    override fun setPlaceData(place: Place) {
        if(!place.photo.equals("url")) {
            val decodedString2 = Base64.decode(place.photo, Base64.DEFAULT)
            val imagenJug2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.size)
            Bitmap.createScaledBitmap(imagenJug2, 90, 90, false)
            val roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(resources, imagenJug2)
            roundedBitmapDrawable2.isCircular = true

            meetingDetailIVplace?.setImageDrawable(roundedBitmapDrawable2)
        }
        meetingDetailTVplaceTitle.text = place.name
        meetingDetailTVaddress.text = place.address

        if(place.openPlace) {
            var openingHours = presenter.getMeetingOpenHours(place)
            if(!openingHours.equals("default")){
                meetingDetailTVopenHours.text = openingHours
            }else{
                meetingDetailTVopenHours.text = getString(R.string.meetingDetailNotOpen)
            }
        }
    }

    override fun clearFriendsData() {
        adapter.memberList.clear()
    }

    override fun setFriendData(friend: User, itsTheUser: Boolean) {
        if(itsTheUser)
            meetingDetailTVjoin.text = getString(R.string.meetingDetailLeave)
        adapter.memberList.add(friend)
        adapter.notifyDataSetChanged()
    }

}
