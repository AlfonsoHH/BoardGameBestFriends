package com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Rule
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class PlaceDetailActivity : AppCompatActivity(), PlaceDetailContract.View, View.OnClickListener {

    private val TAG = "PlaceDetailActivity"
    var placeId = ""
    var kind = ""

    lateinit var menu: Menu

    @Inject
    lateinit var presenter: PlaceDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        setSupportActionBar(placeDetailToolbar)
        supportActionBar!!.setTitle(getString(R.string.placeDetailToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if(savedInstanceState!=null){
            placeId = savedInstanceState.getString("id","")
            kind = savedInstanceState.getString("kind","")
        }else{
            var intent = intent.extras
            placeId = intent.getString("id","")
            kind = intent.getString("kind","")
        }

        injectDependencies()
        presenter.setView(this,placeId)

        placeDetailIVmeetingsIcon.setOnClickListener(this)
        placeDetailIVgamesIcon.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.getId();
        if (id == R.id.placeDetailIVmeetingsIcon) {
            var intent = Intent(applicationContext, TabActivity::class.java)
            intent.putExtra("tab",0)
            intent.putExtra("kind","place-" + placeId)
            startActivity(intent)
        } else if (id == R.id.placeDetailIVgamesIcon) {
            var intent = Intent(applicationContext, GamesActivity::class.java)
            intent.putExtra("kind","place-" + placeId)
            startActivity(intent)
        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null,"")
        super.onDestroy()
    }

    fun modifyGroup(){
        val intent = Intent(this, AddPlaceActivity::class.java)
        intent.putExtra("id",placeId)
        intent.putExtra("action","modify")
        intent.putExtra("kind",kind)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            presenter.getPlaceData(presenter.getUserProfile()!!.regionId,placeId)
            successModify()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setData(place: Place, region: String) {
        if(!place.photo.equals("url")) {
            val decodedString = Base64.decode(place.photo, Base64.DEFAULT)
            val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Bitmap.createScaledBitmap(imagenJug, 90, 90, false)
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
            roundedBitmapDrawable.isCircular = true

            placeDetailIVphoto.setImageDrawable(roundedBitmapDrawable)
        }
        placeDetailTVtitle.text = place.name
        placeDetailTVaddress.text = place.address
        placeDetailTVregion.text = region

        if(!place.ownerId.equals(presenter.getUserProfile()!!.id)){
            var menuItem = menu.findItem(R.id.toolbar_modify)
            menuItem.setVisible(false)
        }
            //TODO ADD A BUTTON TO MODIFY AND CLICK ACCTION

        if(place.openPlace && !place.firstOpeningHours.equals("")) {
            placeDetailFLiconsOpenPlace.setVisibility(true)
            placeDetailLLopen.setVisibility(true)
            placeDetailTVdays1.text = getDay(place.firstOpeningHours!!)
            placeDetailTVhours1.text = getHours(place.firstOpeningHours!!)
            if (!place.secondOpeningHours.equals("")) {
                placeDetailLLopeningHours2.setVisibility(true)
                placeDetailTVdays2.text = getDay(place.secondOpeningHours!!)
                placeDetailTVhours2.text = getHours(place.secondOpeningHours!!)
                if (!place.thirdOpeningHours.equals("")) {
                    placeDetailLLopeningHours3.setVisibility(true)
                    placeDetailTVdays3.text = getDay(place.thirdOpeningHours!!)
                    placeDetailTVhours3.text = getHours(place.thirdOpeningHours!!)
                }
            }
        }
    }

    fun getDay(opening: String): String{
        return opening.substring(0,opening.lastIndexOf("-")-1)
    }

    fun getHours(opening: String): String{
        return opening.substring(opening.lastIndexOf("-")+1,opening.length-1)
    }

    override fun setRuleData(ruleId: Int, ruleNumber: Int) {
        when(ruleNumber){
            1 -> {
                placeDetailTVrule1title.text = resources.getStringArray(R.array.rulesTitle).get(ruleId)
                placeDetailTVrule1subtitle.text = resources.getStringArray(R.array.rulesDescription).get(ruleId)
                placeDetailIVrule1.setImageDrawable(getDrawable(getRulePhoto(ruleId)))
            }
            2 -> {
                placeDetailTVrule2title.text = resources.getStringArray(R.array.rulesTitle).get(ruleId)
                placeDetailTVrule2subtitle.text = resources.getStringArray(R.array.rulesDescription).get(ruleId)
                placeDetailIVrule2.setImageDrawable(getDrawable(getRulePhoto(ruleId)))
            }
            3 -> {
                placeDetailTVrule3title.text = resources.getStringArray(R.array.rulesTitle).get(ruleId)
                placeDetailTVrule3subtitle.text = resources.getStringArray(R.array.rulesDescription).get(ruleId)
                placeDetailIVrule3.setImageDrawable(getDrawable(getRulePhoto(ruleId)))
            }
        }
    }

    fun getRulePhoto(ruleId: Int): Int{
        when(ruleId){
            0 -> return R.drawable.smoke
            1 -> return R.drawable.dontsmoke
            2 -> return R.drawable.pets
            3 -> return R.drawable.dontpets
            4 -> return R.drawable.food
            5 -> return R.drawable.dontfood
            6 -> return R.drawable.dontmobile
            7 -> return R.drawable.snacks
            8 -> return R.drawable.dontsnacks
        }
        return -1
    }

    override fun showErrorPlaces() {
        Toast.makeText(this, getString(R.string.placeDetailErrorPlaces), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRules() {
        Toast.makeText(this, getString(R.string.placeDetailErrorRules), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRegion() {
        Toast.makeText(this, getString(R.string.placeDetailErrorRegion), Toast.LENGTH_SHORT).show()
    }

    override fun successModify() {
        Toast.makeText(this, getString(R.string.placeDetailSuccessModify), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarPlaceDetail.setVisibility(boolean)
        placeDetailFLall.setVisibility(!boolean)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        this.menu = menu

        menu.getItem(1).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_modify -> {
                modifyGroup()
            }
        }
        return true
    }
}
