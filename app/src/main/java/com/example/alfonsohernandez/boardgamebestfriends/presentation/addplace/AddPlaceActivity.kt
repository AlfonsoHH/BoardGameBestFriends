package com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_add_place.*
import javax.inject.Inject

class AddPlaceActivity : AppCompatActivity(), AddPlaceContract.View {

    private val TAG = "AddPlaceActivity"

    private var url: String = "url"

    private var id: String = ""
    private var action: String = ""
    private var kind: String = ""

    lateinit var spinnerArrayAdapterRules: ArrayAdapter<String>

    @Inject
    lateinit var presenter: AddPlacePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        setSupportActionBar(AddPlaceToolbar)
        supportActionBar!!.setTitle(getString(R.string.addPlaceToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if(savedInstanceState != null) {
            id = savedInstanceState.getString("id", "")
            action = savedInstanceState.getString("action", "")
            kind = savedInstanceState.getString("kind", "")
        }else{
            var intent = intent.extras
            id = intent.getString("id", "")
            action = intent.getString("action", "")
            kind = intent.getString("kind", "")
        }

        injectDependencies()
        presenter.setView(this)

        if(action.equals("modify"))
            presenter.getPlace(id)

        addPlaceIVphoto.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, 1)
            }
        })

        addPlaceIVopeningHours.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                openHoursDialog()
            }
        })
    }

    fun openHoursDialog(){
        var builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.dialog_opening_hours,null)
        builder.setView(view)

        val mon1: CheckBox = view.findViewById(R.id.openingHoursCmon1)
        val mon2: CheckBox = view.findViewById(R.id.openingHoursCmon2)
        val mon3: CheckBox = view.findViewById(R.id.openingHoursCmon3)
        val tue1: CheckBox = view.findViewById(R.id.openingHoursCtue1)
        val tue2: CheckBox = view.findViewById(R.id.openingHoursCtue2)
        val tue3: CheckBox = view.findViewById(R.id.openingHoursCtue3)
        val wed1: CheckBox = view.findViewById(R.id.openingHoursCwed1)
        val wed2: CheckBox = view.findViewById(R.id.openingHoursCwed2)
        val wed3: CheckBox = view.findViewById(R.id.openingHoursCwed3)
        val thu1: CheckBox = view.findViewById(R.id.openingHoursCthu1)
        val thu2: CheckBox = view.findViewById(R.id.openingHoursCthu2)
        val thu3: CheckBox = view.findViewById(R.id.openingHoursCthu3)
        val fri1: CheckBox = view.findViewById(R.id.openingHoursCfri1)
        val fri2: CheckBox = view.findViewById(R.id.openingHoursCfri2)
        val fri3: CheckBox = view.findViewById(R.id.openingHoursCfri3)
        val sat1: CheckBox = view.findViewById(R.id.openingHoursCsat1)
        val sat2: CheckBox = view.findViewById(R.id.openingHoursCsat2)
        val sat3: CheckBox = view.findViewById(R.id.openingHoursCsat3)
        val sun1: CheckBox = view.findViewById(R.id.openingHoursCsun1)
        val sun2: CheckBox = view.findViewById(R.id.openingHoursCsun2)
        val sun3: CheckBox = view.findViewById(R.id.openingHoursCsun3)

        val aft1: CheckBox = view.findViewById(R.id.openingHoursCdifAft1)
        val aft2: CheckBox = view.findViewById(R.id.openingHoursCdifAft2)
        val aft3: CheckBox = view.findViewById(R.id.openingHoursCdifAft3)

        val aft1LL: LinearLayout = view.findViewById(R.id.openingHoursLLaft1)
        val aft2LL: LinearLayout = view.findViewById(R.id.openingHoursLLaft2)
        val aft3LL: LinearLayout = view.findViewById(R.id.openingHoursLLaft3)

        val openMor1: EditText = view.findViewById(R.id.openingHoursETopenMor1)
        val openMor2: EditText = view.findViewById(R.id.openingHoursETopenMor2)
        val openMor3: EditText = view.findViewById(R.id.openingHoursETopenMor3)
        val openAft1: EditText = view.findViewById(R.id.openingHoursETopenAft1)
        val openAft2: EditText = view.findViewById(R.id.openingHoursETopenAft2)
        val openAft3: EditText = view.findViewById(R.id.openingHoursETopenAft3)
        val closeMor1: EditText = view.findViewById(R.id.openingHoursETcloseMor1)
        val closeMor2: EditText = view.findViewById(R.id.openingHoursETcloseMor2)
        val closeMor3: EditText = view.findViewById(R.id.openingHoursETcloseMor3)
        val closeAft1: EditText = view.findViewById(R.id.openingHoursETcloseAft1)
        val closeAft2: EditText = view.findViewById(R.id.openingHoursETcloseAft2)
        val closeAft3: EditText = view.findViewById(R.id.openingHoursETcloseAft3)

        aft1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(aft1.isChecked){
                    aft1LL.setVisibility(true)
                }else{
                    aft1LL.setVisibility(false)
                }
            }
        })

        aft2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(aft2.isChecked){
                    aft2LL.setVisibility(true)
                }else{
                    aft2LL.setVisibility(false)
                }
            }
        })

        aft3.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(aft3.isChecked){
                    aft3LL.setVisibility(true)
                }else{
                    aft3LL.setVisibility(false)
                }
            }
        })

        builder.setPositiveButton(getString(R.string.openingHoursAccept),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                convertOpeningHours(0,mon1.isChecked,tue1.isChecked,wed1.isChecked,thu1.isChecked,fri1.isChecked,sat1.isChecked,sun1.isChecked,openMor1.text.toString(),closeMor1.text.toString(),aft1.isChecked,openAft1.text.toString(),closeAft1.text.toString())
                convertOpeningHours(1,mon2.isChecked,tue2.isChecked,wed2.isChecked,thu2.isChecked,fri2.isChecked,sat2.isChecked,sun2.isChecked,openMor2.text.toString(),closeMor2.text.toString(),aft2.isChecked,openAft2.text.toString(),closeAft2.text.toString())
                convertOpeningHours(2,mon3.isChecked,tue3.isChecked,wed3.isChecked,thu3.isChecked,fri3.isChecked,sat3.isChecked,sun3.isChecked,openMor3.text.toString(),closeMor3.text.toString(),aft3.isChecked,openAft3.text.toString(),closeAft3.text.toString())
            }
        })
        builder.setNegativeButton(getString(R.string.openingHoursCancel),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
            }
        })
        var dialog: Dialog = builder.create()
        dialog.show()
    }

    fun convertOpeningHours(timetable: Int,
                            mon: Boolean,
                            tue: Boolean,
                            wed: Boolean,
                            thu: Boolean,
                            fri: Boolean,
                            sat: Boolean,
                            sun: Boolean,
                            openMor: String,
                            closeMor: String,
                            aftDif: Boolean,
                            openAft: String,
                            closeAft: String){
        when(timetable){
            0 -> {
                if((mon || tue || wed || thu || fri || sat || sun) && !openMor.equals("") && !closeMor.equals("")) {
                    addPlaceLLopeningHours1.setVisibility(true)
                    addPlaceTVdays1.text = presenter.convertDays(presenter.generateDayList(mon, tue, wed, thu, fri, sat, sun))
                    addPlaceTVhours1.text = presenter.convertHours(openMor, closeMor, aftDif, openAft, closeAft)
                }else{
                    addPlaceLLopeningHours1.setVisibility(false)
                    addPlaceTVdays1.text = ""
                    addPlaceTVhours1.text = ""
                }
            }
            1 -> {
                if((mon || tue || wed || thu || fri || sat || sun) && !openMor.equals("") && !closeMor.equals("")) {
                    addPlaceLLopeningHours2.setVisibility(true)
                    addPlaceTVdays2.text = presenter.convertDays(presenter.generateDayList(mon, tue, wed, thu, fri, sat, sun))
                    addPlaceTVhours2.text = presenter.convertHours(openMor, closeMor, aftDif, openAft, closeAft)
                }else{
                    addPlaceLLopeningHours2.setVisibility(false)
                    addPlaceTVdays2.text = ""
                    addPlaceTVhours2.text = ""
                }
            }
            2 -> {
                if((mon || tue || wed || thu || fri || sat || sun) && !openMor.equals("") && !closeMor.equals("")) {
                    addPlaceLLopeningHours3.setVisibility(true)
                    addPlaceTVdays3.text = presenter.convertDays(presenter.generateDayList(mon, tue, wed, thu, fri, sat, sun))
                    addPlaceTVhours3.text = presenter.convertHours(openMor, closeMor, aftDif, openAft, closeAft)
                }else{
                    addPlaceLLopeningHours3.setVisibility(false)
                    addPlaceTVdays3.text = ""
                    addPlaceTVhours3.text = ""
                }
            }
        }
    }

    override fun getDayString(day: Int): String{
        when(day){
            0 -> return getString(R.string.openingHoursMon)
            1 -> return getString(R.string.openingHoursTue)
            2 -> return getString(R.string.openingHoursWed)
            3 -> return getString(R.string.openingHoursThu)
            4 -> return getString(R.string.openingHoursFri)
            5 -> return getString(R.string.openingHoursSat)
            6 -> return getString(R.string.openingHoursSun)
        }
        return ""
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    fun addModifyPlace(){
        if(!addPlaceETtitle.text.toString().equals("") && !addPlaceETaddress.text.toString().equals("") && !url.equals("url")) {
            var open: Boolean = false
            var hours1: String? = null
            var hours2: String? = null
            var hours3: String? = null
            if (!kind.equals("My Place")) {
                open = true
                if (!addPlaceTVhours1.equals(""))
                    hours1 = addPlaceTVdays1.text.toString() + "_" + addPlaceTVhours1.text.toString()
                if (!addPlaceTVhours2.equals(""))
                    hours2 = addPlaceTVdays2.text.toString() + "_" + addPlaceTVhours2.text.toString()
                if (!addPlaceTVhours3.equals(""))
                    hours3 = addPlaceTVdays3.text.toString() + "_" + addPlaceTVhours3.text.toString()
            }
            if (action.equals("modify")) {
                presenter.modifyPlace(this,Place(id, url, addPlaceETtitle.text.toString(), addPlaceETaddress.text.toString(),0.0,0.0,addPlaceSpinner1.selectedItemId.toInt(), addPlaceSpinner2.selectedItemId.toInt(), addPlaceSpinner3.selectedItemId.toInt(), presenter.getUserProfile()!!.id, open, hours1, hours2, hours3))
            } else {
                presenter.savePlace(this,Place(id, url, addPlaceETtitle.text.toString(), addPlaceETaddress.text.toString(),0.0,0.0,addPlaceSpinner1.selectedItemId.toInt(), addPlaceSpinner2.selectedItemId.toInt(), addPlaceSpinner3.selectedItemId.toInt(), presenter.getUserProfile()!!.id, open, hours1, hours2, hours3))
            }
        }else{
            showErrorEmpty()
        }
    }

    override fun setData(place: Place) {
        if(!place.photo.equals("url")) {
            val decodedString = Base64.decode(place.photo, Base64.DEFAULT)
            val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Bitmap.createScaledBitmap(imagenJug, 90, 90, false)
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
            roundedBitmapDrawable.isCircular = true

            addPlaceIVphoto.setImageDrawable(roundedBitmapDrawable)

            url = place.photo
        }

        addPlaceETtitle.setText(place.name)
        addPlaceETaddress.setText(place.address)
        if(!kind.equals("My Place")){
            if(!place.firstOpeningHours!!.equals("")) {
                addPlaceLLopeningHours1.setVisibility(true)
                addPlaceTVdays1.text = place.firstOpeningHours!!.substring(0, place.firstOpeningHours!!.lastIndexOf("_") - 1)
                addPlaceTVhours1.text = place.firstOpeningHours!!.substring(place.firstOpeningHours!!.lastIndexOf("_") + 1,place.firstOpeningHours!!.length-1)
            }
            if(!place.secondOpeningHours!!.equals("")) {
                addPlaceLLopeningHours2.setVisibility(true)
                addPlaceTVdays2.text = place.firstOpeningHours!!.substring(0, place.firstOpeningHours!!.lastIndexOf("_") - 1)
                addPlaceTVhours2.text = place.firstOpeningHours!!.substring(place.firstOpeningHours!!.lastIndexOf("_") + 1,place.firstOpeningHours!!.length-1)
            }
            if(!place.thirdOpeningHours!!.equals("")) {
                addPlaceLLopeningHours3.setVisibility(true)
                addPlaceTVdays3.text = place.firstOpeningHours!!.substring(0, place.firstOpeningHours!!.lastIndexOf("_") - 1)
                addPlaceTVhours3.text = place.firstOpeningHours!!.substring(place.firstOpeningHours!!.lastIndexOf("_") + 1,place.firstOpeningHours!!.length-1)
            }
        }
        addPlaceSpinner1.setSelection(place.firstRuleId)
        addPlaceSpinner2.setSelection(place.secondRuleId)
        addPlaceSpinner3.setSelection(place.thirdRuleId)
    }

    override fun setSpinnerData() {
        spinnerArrayAdapterRules = ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.rulesTitle))
        spinnerArrayAdapterRules.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addPlaceSpinner1.setAdapter(spinnerArrayAdapterRules)
        addPlaceSpinner2.setAdapter(spinnerArrayAdapterRules)
        addPlaceSpinner3.setAdapter(spinnerArrayAdapterRules)

        if(!kind.equals("My Place")){
            addPlaceLLopen.setVisibility(true)
        }
    }

    override fun finishAddPlace() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun showErrorAdding() {
        Toast.makeText(this, getString(R.string.addPlaceErrorAddingPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorCity() {
        Toast.makeText(this, getString(R.string.addPlaceErrorCity), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorUnknown() {
        Toast.makeText(this, getString(R.string.addPlaceErrorUnknown), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRules() {
        Toast.makeText(this, getString(R.string.addingPlaceErrorRules), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingPlace() {
        Toast.makeText(this, getString(R.string.addPlaceErrorAddingPlace), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddingPlaceToUser() {
        Toast.makeText(this, getString(R.string.addPlaceErrorAddingPlaceToUser), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorModify() {
        Toast.makeText(this, getString(R.string.addPlaceErrorModifying), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmpty() {
        Toast.makeText(this, getString(R.string.addPlaceErrorEmpty), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRegion() {
        Toast.makeText(this, getString(R.string.addPlaceErrorRegion), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAddress() {
        Toast.makeText(this, getString(R.string.addPlaceErrorAddress), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarAddPlace.setVisibility(boolean)
        addPlaceFLall.setVisibility(!boolean)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            val pickedImage = data.data
            val roundedDrawable = RoundedBitmapDrawableFactory.create(applicationContext.resources, BitmapFactory.decodeFile(presenter.getRealPathFromURI(applicationContext, pickedImage)))
            roundedDrawable.isCircular = true
            addPlaceIVphoto.setImageDrawable(roundedDrawable)
            addPlaceIVphoto.adjustViewBounds = true

            url = presenter.getUrlFromPhoto(contentResolver.query(pickedImage!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        menu.getItem(0).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_add -> {
                addModifyPlace()
            }
        }
        return true
    }
}
