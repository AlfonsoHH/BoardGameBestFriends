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
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import kotlinx.android.synthetic.main.activity_add_meeting.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddMeetingActivity : AppCompatActivity(), AddMeetingContract.View {

    private val TAG = "AddMeetingActivity"

    @Inject
    lateinit var presenter: AddMeetingPresenter

    lateinit var spinnerArrayAdapterGroup: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterPlace: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterGame: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)

        setSupportActionBar(addMeetingToolbar)
        supportActionBar?.setTitle(getString(R.string.addMeetingToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        setupSpinners()
        presenter.setView(this)
        getDate()

//        if(action.equals("modify")) {
//            addMeetingLLwho.setVisibility(false)
//            addMeetingFLwho.setVisibility(false)
//            presenter.getMeetingData()
//        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    fun addMeeting(){
        if(!addMeetingTVhour.text.toString().equals("") && !addMeetingTVdate.text.toString().equals("") && !addMeetingETtitle.text.toString().equals("") && !addMeetingETdescription.text.toString().equals("")) {

            if(presenter.getMyPlacee()!=null || !addMeetingSwhere.selectedItem.toString().equals(getString(R.string.addMeetingMyPlace))) {

                val actualGame = presenter.getGameFromTitle(addMeetingSwhat.selectedItem.toString())

                var actualPlace = Place()

                if (!addMeetingSwhere.selectedItem.toString().equals(getString(R.string.addMeetingMyPlace))){
                    presenter.getPlaceFromTitle(addMeetingSwhere.selectedItem.toString())?.let{
                        actualPlace = it
                    }
                }else {
                    presenter.myPlace?.let{
                        actualPlace = it
                    }
                }
                var actualGroup = Group()

                if (!addMeetingSwho.selectedItem.toString().equals(getString(R.string.addMeetingOpen))) {
                    presenter.getGroupFromTitle(addMeetingSwho.selectedItem.toString())?.let{
                        actualGroup = it
                    }
                } else {
                    actualGroup.id = "open"
                }

                var vacants = actualGame.maxPlayers

                if (addMeetingCheckbox.isChecked)
                    vacants = vacants - 1

                presenter.getUserProfile()?.let {user ->
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
                }
            }else{
                showError(R.string.addMeetingErrorMyPlace)
            }
        }else{
            showError(R.string.addMeetingErrorEmpty)
        }
    }

    override fun finishAddMeeting() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun getDate(){

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm")

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            addMeetingTVhour.text = sdf.format(cal.time)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            addMeetingTVdate.text = sdf.format(cal.time)

            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        addMeetingFLwhen.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun setupSpinners(){
        //GROUPS SPINNER
        val groupList = presenter.getUserGroups()
        groupList.add(0,"Open")

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
        placeList.add(0,"My Place")

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

        spinnerArrayAdapterGame = ArrayAdapter(this, android.R.layout.simple_spinner_item, gameList)
        spinnerArrayAdapterGame.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwhat.setAdapter(spinnerArrayAdapterGame)
    }

    fun spinnerItemChange(){
        spinnerArrayAdapterGame.clear()
        presenter.getUserProfile()?.let {
            presenter.getUserGames(it.id)
        }
        if(!addMeetingSwho.selectedItem.toString().equals("Open")) {
            val group = presenter.getGroupFromTitle(addMeetingSwho.selectedItem.toString())
            if (group != null)
                presenter.getGroupGames(group.id)
        }
        if(!addMeetingSwhere.selectedItem.toString().equals("My place")) {
            val place = presenter.getPlaceFromTitle(addMeetingSwhere.selectedItem.toString())
            if (place != null)
                presenter.getPlaceGames(place.id)
        }
    }

    override fun addGameToSpinner(gamesTitle: String) {
        if(!itsGame(gamesTitle))
            spinnerArrayAdapterGame.add(gamesTitle)
    }

    override fun addGamesToSpinner(games: ArrayList<Game>) {
        for(game in games) {
            if (!itsGame(game.title))
                spinnerArrayAdapterGame.add(game.title)
        }
    }

    override fun itsGame(game: String): Boolean {
        for(i in 0..spinnerArrayAdapterGame.count-1){
            if(game.equals(spinnerArrayAdapterGame.getItem(i)))
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
