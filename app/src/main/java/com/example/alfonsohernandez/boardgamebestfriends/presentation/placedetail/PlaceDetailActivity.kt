package com.example.alfonsohernandez.boardgamebestfriends.presentation.placedetail

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addplace.AddPlaceActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BaseNotificationActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.messaging.RemoteMessage
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_place_detail.*
import javax.inject.Inject

class PlaceDetailActivity : BaseNotificationActivity(),
        PlaceDetailContract.View,
        View.OnClickListener {

    private val TAG = "PlaceDetailActivity"
    var placeId = ""
    var kind = ""

    var iAmTheCreator = false

    @Inject
    lateinit var presenter: PlaceDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        setSupportActionBar(placeDetailToolbar)
        supportActionBar?.setTitle(getString(R.string.placeDetailToolbarTitle))
        supportActionBar?.setIcon(R.drawable.icono_bgbf)

        val intent = intent.extras
        intent?.let {
            placeId = it.getString("id", "")
            kind = it.getString("kind", "")
        }

        injectDependencies()
        presenter.setView(this, placeId)

        placeDetailIVmeetingsIcon.setOnClickListener(this)
        placeDetailIVgamesIcon.setOnClickListener(this)
    }

    override fun showNotification(rm: RemoteMessage) {
        setNotificacion(rm)
        allNotifications()
    }

    override fun onClick(v: View?) {
        val id = v?.getId()
        if (id == R.id.placeDetailIVmeetingsIcon) {
            val intent = Intent(applicationContext, TabActivity::class.java)
            intent.putExtra("tab", 0)
            intent.putExtra("kind", "place-" + placeId)
            startActivity(intent)
        } else if (id == R.id.placeDetailIVgamesIcon) {
            val intent = Intent(applicationContext, GamesActivity::class.java)
            intent.putExtra("kind", "place-" + placeId)
            startActivity(intent)
        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    fun modifyGroup() {
        val intent = Intent(this, AddPlaceActivity::class.java)
        intent.putExtra("id", placeId)
        intent.putExtra("action", "modify")
        intent.putExtra("kind", kind)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            presenter.getData(placeId)
            showSuccess(R.string.placeDetailSuccessModify)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setData(place: Place, region: String) {
        if (!place.photo.equals("url")) {
            Glide.with(this)
                    .load(place.photo)
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(placeDetailIVphoto)
        }

        placeDetailTVtitle.text = place.name
        placeDetailTVaddress.text = place.address
        placeDetailTVregion.text = region

        if (place.ownerId.equals(presenter.getUserProfile()?.id)) {
            iAmTheCreator = true
        }

        if (place.openPlace) {
            placeDetailFLiconsOpenPlace.setVisibility(true)
            placeDetailLLopen.setVisibility(true)
            place.firstOpeningHours?.let {
                if (!it.equals("")) {

                    placeDetailLLopeningHours1.setVisibility(true)
                    placeDetailTVdays1.text = presenter.getDay(it)
                    placeDetailTVhours1.text = presenter.getHours(it)
                }
            }
            place.secondOpeningHours?.let {
                if (!it.equals("")) {
                    placeDetailLLopeningHours2.setVisibility(true)
                    placeDetailTVdays2.text = presenter.getDay(it)
                    placeDetailTVhours2.text = presenter.getHours(it)
                }
            }
            place.thirdOpeningHours?.let {
                if (!it.equals("")) {
                    placeDetailLLopeningHours3.setVisibility(true)
                    placeDetailTVdays3.text = presenter.getDay(it)
                    placeDetailTVhours3.text = presenter.getHours(it)
                }
            }
        }
    }

    override fun setRuleData(ruleId: Int, ruleNumber: Int) {
        when (ruleNumber) {
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

    fun getRulePhoto(ruleId: Int): Int {
        when (ruleId) {
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

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarPlaceDetail?.setVisibility(isLoading)
        placeDetailFLall?.setVisibility(!isLoading)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        if (iAmTheCreator)
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
