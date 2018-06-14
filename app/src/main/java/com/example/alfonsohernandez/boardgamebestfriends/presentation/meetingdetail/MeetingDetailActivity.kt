package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting.AddMeetingActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.messaging.RemoteMessage
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_meeting_detail.*
import javax.inject.Inject


class MeetingDetailActivity : AppCompatActivity(),
        MeetingDetailContract.View,
        View.OnClickListener{

    private val TAG = "MeetingDetailActivity"
    var meetingId: String = ""
    var iAmTheCreator = false

    var adapter = AdapterMembers()

    @Inject
    lateinit var presenter: MeetingDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_detail)

        setSupportActionBar(meetingDetailToolbar)
        supportActionBar?.setTitle(getString(R.string.meetingDetailToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        showProgress(true)

        val intent = intent.extras
        intent?.let {
            meetingId = it.getString("id", "")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this,meetingId)

        meetingDetailTVjoin.setOnClickListener(this)
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(this,rm)
        nf.chat()
        nf.groupUser()
        nf.groupRemoved()
        nf.meetingModified()
        nf.meetingRemoved()
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
        meetingDetailTVdate.text = meeting.date.substring(meeting.date.lastIndexOf("_")+1,meeting.date.length)
        meetingDetailTVvacants.text = meeting.vacants.toString() + getString(R.string.meetingDetailVacants)

        meetingDetailTVjoin.text = getString(R.string.meetingDetailJoin)

        if (meeting.creatorId.equals(presenter.getUserProfile()?.id)) {
            iAmTheCreator = true
        }
    }

    fun modifyMeeting() {
        val intent = Intent(this, AddMeetingActivity::class.java)
        intent.putExtra("id", meetingId)
        intent.putExtra("action", "modify")
        startActivityForResult(intent, 1)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.meetingDetailTVjoin ->{
                if(meetingDetailTVjoin.text.toString().equals(getString(R.string.meetingDetailJoin))) {
                    presenter.joinGame(true)
                    val vacant = meetingDetailTVvacants.text.substring(0,1).toInt() - 1
                    meetingDetailTVvacants.text = vacant.toString() + meetingDetailTVvacants.text.substring(1,meetingDetailTVvacants.text.length)
                    meetingDetailTVjoin.text = getString(R.string.meetingDetailLeave)
                }else {
                    presenter.joinGame(false)
                    val vacant = meetingDetailTVvacants.text.substring(0,1).toInt() + 1
                    meetingDetailTVvacants.text = vacant.toString() + meetingDetailTVvacants.text.substring(1,meetingDetailTVvacants.text.length)
                    meetingDetailTVjoin.text = getString(R.string.meetingDetailJoin)
                }
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
        progressBarMeetingDetail?.setVisibility(isLoading)
        meetingDetailFLall?.setVisibility(!isLoading)
    }

    override fun setupRecycler() {
        meetingDetailRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        meetingDetailRV.adapter = adapter
    }

    override fun setGameData(game: Game) {
        Glide.with(this)
                .load(game.photo)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(meetingDetailIVgame)

        meetingDetailTVgameTitle.text = game.title
        meetingDetailRatingBar.rating = game.rating.toFloat()
        meetingDetailTVplayers.text = game.minPlayers.toString() + " - " + game.maxPlayers.toString()
        meetingDetailTVduration.text = game.playingTime.toString()
    }

    override fun setPlaceData(place: Place) {
        if(!place.photo.equals("url")) {
            Glide.with(this)
                    .load(place.photo)
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(meetingDetailIVplace)
        }
        meetingDetailTVplaceTitle.text = place.name
        meetingDetailTVaddress.text = place.address

        if(place.openPlace) {
            val openingHours = presenter.getMeetingOpenHours(place)
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

    override fun removeFriendData(friend: User, itsTheUser: Boolean) {
        if(itsTheUser)
            meetingDetailTVjoin.text = getString(R.string.meetingDetailJoin)
        adapter.memberList.remove(friend)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        if (iAmTheCreator)
            menu.getItem(1).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_modify -> { modifyMeeting() }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            presenter.getMeetingData(meetingId)
            showSuccess(R.string.meetingDetailSuccessModify)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
