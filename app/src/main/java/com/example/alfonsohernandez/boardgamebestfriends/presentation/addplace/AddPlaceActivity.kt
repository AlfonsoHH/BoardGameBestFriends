package com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePermissionActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.OpenHoursDialog
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_add_place.*
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddPlaceActivity : BasePermissionActivity(),
        AddPlaceContract.View,
        View.OnClickListener,
        BasePermissionActivity.PermissionCallback,
        OpenHoursDialog.OpeningHoursCallback {

    private val TAG = "AddPlaceActivity"

    private var url: String = "url"
    private var id: String = ""
    private var action: String = ""
    private var kind: String = ""

    var photoModified = false

    lateinit var spinnerArrayAdapterRules: ArrayAdapter<String>

    @Inject
    lateinit var presenter: AddPlacePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        setSupportActionBar(AddPlaceToolbar)
        supportActionBar?.setTitle(getString(R.string.addPlaceToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        val intent = intent.extras
        id = intent.getString("id", "")
        action = intent.getString("action", "")
        kind = intent.getString("kind", "")

        injectDependencies()
        presenter.setView(this)

        if(action.equals("modify"))
            presenter.getPlace(id)

        super.callbackPermissions = this
        addPlaceIVphoto.setOnClickListener(this)
        addPlaceIVopeningHours.setOnClickListener(this)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onOpeningHoursChoosed(openingHours: ArrayList<String>) {
        setOpeningHours(openingHours)
    }

    fun setOpeningHours(list: ArrayList<String>){
        addPlaceTVdays1.text = list.get(0)
        addPlaceTVhours1.text = list.get(1)
        addPlaceTVdays2.text = list.get(2)
        addPlaceTVhours2.text = list.get(3)
        addPlaceTVdays3.text = list.get(4)
        addPlaceTVhours3.text = list.get(5)

        if(!list.get(0).equals("") && !list.get(1).equals(""))
            addPlaceLLopeningHours1.setVisibility(true)
        else
            addPlaceLLopeningHours1.setVisibility(false)

        if(!list.get(2).equals("") && !list.get(3).equals(""))
            addPlaceLLopeningHours2.setVisibility(true)
        else
            addPlaceLLopeningHours2.setVisibility(false)

        if(!list.get(4).equals("") && !list.get(5).equals(""))
            addPlaceLLopeningHours3.setVisibility(true)
        else
            addPlaceLLopeningHours3.setVisibility(false)

    }

    fun addModifyPlace(){
        if(!addPlaceETtitle.text.toString().equals("") && !addPlaceETaddress.text.toString().equals("") && (!url.equals("url") || photoModified)) {
            var open: Boolean = false
            var hours1: String? = null
            var hours2: String? = null
            var hours3: String? = null
            if (!kind.equals("My Place")) {
                open = true
                if (!addPlaceTVhours1.text.toString().equals(""))
                    hours1 = addPlaceTVdays1.text.toString() + "_" + addPlaceTVhours1.text.toString()
                if (!addPlaceTVhours2.text.toString().equals(""))
                    hours2 = addPlaceTVdays2.text.toString() + "_" + addPlaceTVhours2.text.toString()
                if (!addPlaceTVhours3.text.toString().equals(""))
                    hours3 = addPlaceTVdays3.text.toString() + "_" + addPlaceTVhours3.text.toString()
            }
            addPlaceIVphoto.setDrawingCacheEnabled(true)
            addPlaceIVphoto.buildDrawingCache()
            val bitmap = (addPlaceIVphoto.getDrawable() as BitmapDrawable).getBitmap()

            presenter.getUserProfile()?.let {
                val place = Place(id, url,
                        addPlaceETtitle.text.toString(), addPlaceETaddress.text.toString(),
                        0.0, 0.0,
                        addPlaceSpinner1.selectedItemId.toInt(), addPlaceSpinner2.selectedItemId.toInt(), addPlaceSpinner3.selectedItemId.toInt(),
                        it.id,
                        open,
                        hours1, hours2, hours3)

                if (action.equals("modify")) {
                    presenter.saveImage(place,
                            bitmap,
                            photoModified,
                            true)
                } else {
                    presenter.saveImage(place,
                            bitmap,
                            photoModified,
                            false)
                }
            }
        }else{
            showError(R.string.addPlaceErrorEmpty)
        }
    }

    override fun onImageReceived(intent: Intent, fromGallery: Boolean) {
        if(fromGallery)
            setPhotoImage(File(presenter.getRealPathFromURI(intent.data)))
        else
            setPhotoImage(intent.extras.get("data") as Bitmap)
    }

    override fun setPhotoImage(image: Any) {
        Glide.with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(addPlaceIVphoto)
    }

    override fun setData(place: Place) {
        if(!place.photo.equals("url")) {
            Glide.with(this)
                    .load(place.photo)
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(addPlaceIVphoto)
            url = place.photo
        }

        addPlaceETtitle.setText(place.name)
        addPlaceETaddress.setText(place.address)

        if(!kind.equals("My Place")){
            place.firstOpeningHours?.let {
                if (!it.equals("")) {
                    addPlaceLLopeningHours1.setVisibility(true)
                    addPlaceTVdays1.text = it.substring(0, it.lastIndexOf("_") - 1)
                    addPlaceTVhours1.text = it.substring(it.lastIndexOf("_") + 1, it.length - 1)
                }
            }
            place.secondOpeningHours?.let {
                if (!it.equals("")) {
                    addPlaceLLopeningHours2.setVisibility(true)
                    addPlaceTVdays2.text = it.substring(0, it.lastIndexOf("_") - 1)
                    addPlaceTVhours2.text = it.substring(it.lastIndexOf("_") + 1, it.length - 1)
                }
            }
            place.thirdOpeningHours?.let {
                if (!it.equals("")) {
                    addPlaceLLopeningHours3.setVisibility(true)
                    addPlaceTVdays3.text = it.substring(0, it.lastIndexOf("_") - 1)
                    addPlaceTVhours3.text = it.substring(it.lastIndexOf("_") + 1, it.length - 1)
                }
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addPlaceIVphoto -> {
                askForPermissionsPhoto(this@AddPlaceActivity)
            }
            R.id.addPlaceIVopeningHours ->{
                OpenHoursDialog.callbackOpeningHours = this@AddPlaceActivity
                OpenHoursDialog.createDialog(this@AddPlaceActivity)
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
        progressBarAddPlace?.setVisibility(isLoading)
        addPlaceFLall?.setVisibility(!isLoading)
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
