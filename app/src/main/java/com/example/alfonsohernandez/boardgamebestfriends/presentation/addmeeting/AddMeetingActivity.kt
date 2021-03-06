package com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.messaging.RemoteMessage
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_add_meeting.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddMeetingActivity : BaseNotificationActivity(), AddMeetingContract.View {

    private val TAG = "AddMeetingActivity"

    @Inject
    lateinit var presenter: AddMeetingPresenter

    lateinit var spinnerArrayAdapterGroup: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterPlace: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterGame: ArrayAdapter<String>

    var meetingId = ""
    var action = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)

        //COMMENT RANDOM TO TEST

        setSupportActionBar(addMeetingToolbar)
        supportActionBar?.setTitle(getString(R.string.addMeetingToolbarTitle))
        supportActionBar?.setIcon(R.drawable.icono_bgbf)

        injectDependencies()
        setupSpinners()
        presenter.setView(this)
        getDate()

        val intent = intent.extras
        intent?.let {
            meetingId = it.getString("id", "")
            action = it.getString("action", "")
        }

        if (action.equals("modify")) {
            addMeetingSwho.setVisibility(false)
            addMeetingSwhere.setVisibility(false)
            addMeetingSwhat.setVisibility(false)
            addMeetingCheckbox.setVisibility(false)
            presenter.getMeetingData(meetingId)
            supportActionBar?.setTitle(getString(R.string.addMeetingToolbarTitleModify))
        }
    }

    override fun showNotification(rm: RemoteMessage) {
        setNotificacion(rm)
        allNotifications()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    override fun setData(meeting: Meeting, place: Place, group: Group, game: Game) {
        addMeetingTVhour.text = meeting.date.substring(0, 5)
        addMeetingTVdate.text = meeting.date.substring(6, meeting.date.length)
        addMeetingETtitle.setText(meeting.title)
        addMeetingETdescription.setText(meeting.description)
        if(!group.photo.equals("url")) {
            Glide.with(this)
                    .load(group.photo)
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(addMeetingIVwho)
        }
        addMeetingTVwho.setVisibility(true)
        addMeetingTVwho.text = group.title
        Glide.with(this)
                .load(place.photo)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(addMeetingIVwhere)
        addMeetingTVwhere.setVisibility(true)
        addMeetingTVwhere.text = place.name
        Glide.with(this)
                .load(game.photo)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(addMeetingIVwhat)
        addMeetingTVwhat.setVisibility(true)
        addMeetingTVwhat.text = game.title
    }

    fun addMeeting() {
        if(!addMeetingSwhat.selectedItem.toString().equals(getString(R.string.addMeetingNoPersonalGames))) {
            if (!addMeetingTVhour.text.toString().equals(getString(R.string.addMeetingErrorNoGames)) && !addMeetingTVdate.text.toString().equals("") && !addMeetingETtitle.text.toString().equals("") && !addMeetingETdescription.text.toString().equals("")) {

                if (presenter.getMyPlacee() != null || !addMeetingSwhere.selectedItem.toString().equals(getString(R.string.addMeetingMyPlace))) {

                    presenter.getUserProfile()?.let { user ->
                        if (!action.equals("modify")) {

                            val actualGame = presenter.getGameFromTitle(addMeetingSwhat.selectedItem.toString())

                            var actualPlace = Place()

                            if (!addMeetingSwhere.selectedItem.toString().equals(getString(R.string.addMeetingMyPlace))) {
                                presenter.getPlaceFromTitle(addMeetingSwhere.selectedItem.toString())?.let {
                                    actualPlace = it
                                }
                            } else {
                                presenter.myPlace?.let {
                                    actualPlace = it
                                }
                            }
                            var actualGroup = Group()

                            if (!addMeetingSwho.selectedItem.toString().equals(getString(R.string.addMeetingOpen))) {
                                presenter.getGroupFromTitle(addMeetingSwho.selectedItem.toString())?.let {
                                    actualGroup = it
                                }
                            } else {
                                actualGroup.id = getString(R.string.addMeetingOpen)
                            }

                            var vacants = actualGame.maxPlayers

                            if (addMeetingCheckbox.isChecked)
                                vacants = vacants - 1

                            presenter.saveMeeting(Meeting("",
                                    addMeetingETtitle.text.toString(),
                                    addMeetingETdescription.text.toString(),
                                    addMeetingTVhour.text.toString() + "_" + addMeetingTVdate.text.toString(),
                                    actualGroup.id,
                                    actualPlace.id,
                                    actualPlace.photo,
                                    actualGame.id,
                                    actualGame.photo,
                                    user.id,
                                    vacants),
                                    addMeetingCheckbox.isChecked)
                        } else {
                            presenter.modifyMeeting(Meeting(meetingId,
                                    addMeetingETtitle.text.toString(),
                                    addMeetingETdescription.text.toString(),
                                    addMeetingTVhour.text.toString() + "_" + addMeetingTVdate.text.toString(),
                                    "", "", "", "", "",
                                    user.id,
                                    0),
                                    false)
                        }
                    }
                } else {
                    showError(R.string.addMeetingErrorMyPlace)
                }
            } else {
                showError(R.string.addMeetingErrorEmpty)
            }
        }else{
            showError(R.string.addMeetingErrorNoGames)
        }
    }

    override fun finishAddMeeting() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun getDate() {

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm")
        val sdfDays = SimpleDateFormat("dd/MM/yy")

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            addMeetingTVhour.text = sdf.format(cal.time)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            addMeetingTVdate.text = sdfDays.format(cal.time)

            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        addMeetingFLwhen.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun setupSpinners() {
        //GROUPS SPINNER
        val groupList = presenter.getUserGroups()
        groupList.add(0, getString(R.string.addMeetingOpen))

        spinnerArrayAdapterGroup = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupList)
        spinnerArrayAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwho.setAdapter(spinnerArrayAdapterGroup)

        addMeetingSwho.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerItemChange()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        //PLACES SPINNER
        val placeList = presenter.getPlaces()
        placeList.add(0, getString(R.string.addMeetingMyPlace))

        spinnerArrayAdapterPlace = ArrayAdapter(this, android.R.layout.simple_spinner_item, placeList)
        spinnerArrayAdapterPlace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwhere.setAdapter(spinnerArrayAdapterPlace)

        addMeetingSwhere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerItemChange()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        //GAMES SPINNER
        val gameList: MutableList<String> = mutableListOf()
        gameList.add(getString(R.string.addMeetingNoPersonalGames))

        spinnerArrayAdapterGame = ArrayAdapter(this, android.R.layout.simple_spinner_item, gameList)
        spinnerArrayAdapterGame.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwhat.setAdapter(spinnerArrayAdapterGame)
    }

    fun spinnerItemChange() {
        presenter.getUserProfile()?.let {
            presenter.getUserGames(it.id)
        }
        if (!addMeetingSwho.selectedItem.toString().equals(getString(R.string.addMeetingOpen))) {
            val group = presenter.getGroupFromTitle(addMeetingSwho.selectedItem.toString())
            if (group != null)
                presenter.getGroupGames(group.id)
        }
        if (!addMeetingSwhere.selectedItem.toString().equals(getString(R.string.addMeetingMyPlace))) {
            val place = presenter.getPlaceFromTitle(addMeetingSwhere.selectedItem.toString())
            if (place != null)
                presenter.getPlaceGames(place.id)
        }
    }

    override fun addGameToSpinner(gamesTitle: String) {
        if (!itsGame(gamesTitle))
            spinnerArrayAdapterGame.add(gamesTitle)
    }

    override fun addGamesToSpinner(games: ArrayList<Game>) {
        if(games.size != 0) {
            spinnerArrayAdapterGame.clear()
            for (game in games) {
                if (!itsGame(game.title))
                    spinnerArrayAdapterGame.add(game.title)
            }
        }
    }

    override fun itsGame(game: String): Boolean {
        for (i in 0..spinnerArrayAdapterGame.count - 1) {
            if (game.equals(spinnerArrayAdapterGame.getItem(i)))
                return true
        }
        return false
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarAddMeeting?.setVisibility(isLoading)
        addMeetingFLall?.setVisibility(!isLoading)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        menu.getItem(0).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_add -> {
                addMeeting()
            }
        }
        return true
    }
}
