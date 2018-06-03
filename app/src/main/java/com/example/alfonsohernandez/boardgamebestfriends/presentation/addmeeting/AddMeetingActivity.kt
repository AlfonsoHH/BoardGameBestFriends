package com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.google.android.gms.games.Games
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
        supportActionBar!!.setTitle(getString(R.string.addMeetingToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        setupSpinners()
        presenter.setView(this)
        getDate()
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
                var actualGame = presenter.getGameFromTitle(addMeetingSwhat.selectedItem.toString())
                var actualPlace: Place
                if(!addMeetingSwhere.selectedItem.toString().equals(getString(R.string.addMeetingMyPlace)))
                    actualPlace = presenter.getPlaceFromTitle(addMeetingSwhere.selectedItem.toString())
                else
                    actualPlace = presenter.myPlace!!
                var actualGroup = Group()
                if (!addMeetingSwho.selectedItem.toString().equals(getString(R.string.addMeetingOpen))) {
                    actualGroup = presenter.getGroupFromTitle(addMeetingSwho.selectedItem.toString())
                } else {
                    actualGroup.id = "open"
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
                        presenter.getUserProfile()!!.id,
                        vacants),
                        addMeetingCheckbox.isChecked);
            }else{
                showErrorMyPlace()
            }
        }else{
            showErrorEmpty()
        }
    }

    override fun finishAddMeeting() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun getDate(){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            addMeetingTVhour.text = SimpleDateFormat("HH:mm").format(cal.time)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            addMeetingTVdate.text = SimpleDateFormat("dd/MM/yy").format(cal.time)

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
        val groupList: MutableList<String> = mutableListOf("Open")
        spinnerArrayAdapterGroup = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupList)
        spinnerArrayAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwho.setAdapter(spinnerArrayAdapterGroup)

        addMeetingSwho.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerItemChange()
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        presenter.getUserGroups()

        val placeList: MutableList<String> = mutableListOf("My place")
        spinnerArrayAdapterPlace = ArrayAdapter(this, android.R.layout.simple_spinner_item, placeList)
        spinnerArrayAdapterPlace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwhere.setAdapter(spinnerArrayAdapterPlace)

        addMeetingSwhere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerItemChange()
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        presenter.getOpenPlaces()

        val gameList: MutableList<String> = mutableListOf()
        spinnerArrayAdapterGame = ArrayAdapter(this, android.R.layout.simple_spinner_item, gameList)
        spinnerArrayAdapterGame.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addMeetingSwhat.setAdapter(spinnerArrayAdapterGame)
    }

    fun spinnerItemChange(){
        spinnerArrayAdapterGame.clear()
        presenter.getUserGames(presenter.getUserProfile()!!.id)
        if(!addMeetingSwho.selectedItem.toString().equals("Open"))
            presenter.getGroupGames(presenter.getGroupFromTitle(addMeetingSwho.selectedItem.toString()).id)
        if(!addMeetingSwhere.selectedItem.toString().equals("My place"))
            presenter.getPlaceGames(presenter.getPlaceFromTitle(addMeetingSwhere.selectedItem.toString()).id)
    }

    override fun addGroupToSpinner(groupsTitle: String) {
        spinnerArrayAdapterGroup.add(groupsTitle)
    }

    override fun setPlaceSpinner(placesList: ArrayList<String>) {
        for(place in placesList){
            spinnerArrayAdapterPlace.add(place)
        }
    }

    override fun addGameToSpinner(gamesTitle: String) {
        if(!itsGame(gamesTitle))
            spinnerArrayAdapterGame.add(gamesTitle)
    }

    override fun itsGame(game: String): Boolean {
        for(i in 0..spinnerArrayAdapterGame.count-1){
            if(game.equals(spinnerArrayAdapterGame.getItem(i)))
                return true
        }
        return false
    }

    override fun showErrorAdding() {
        Toast.makeText(this, getString(R.string.addMeetingErrorAddingGroup), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorGames() {
        Toast.makeText(this, getString(R.string.addMeetingErrorGames), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorGroups() {
        Toast.makeText(this, getString(R.string.addMeetingErrorGroups), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorPlaces() {
        Toast.makeText(this, getString(R.string.addMeetingErrorPlaces), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingMeeting() {
        Toast.makeText(this, getString(R.string.addMeetingErrorAdding), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingUserToMeeting() {
        Toast.makeText(this, getString(R.string.addMeetingErrorJoining), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingMeetingToGroup() {
        Toast.makeText(this, getString(R.string.addMeetingErrorAddingGroup), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingMeetingToPlace() {
        Toast.makeText(this, getString(R.string.addMeetingErrorAddingPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmpty() {
        Toast.makeText(this, getString(R.string.addMeetingErrorEmpty), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMyPlace() {
        Toast.makeText(this, getString(R.string.addMeetingErrorMyPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarAddMeeting.setVisibility(boolean)
        addMeetingFLall.setVisibility(!boolean)
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
