package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterTabPager
import kotlinx.android.synthetic.main.activity_tab.*
import javax.inject.Inject
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.mateware.snacky.Snacky
import timber.log.Timber

class TabActivity : AppCompatActivity(),
        TabContract.View,
        DialogFactory.CitySelectedCallback{

    @Inject
    lateinit var presenter: TabPresenter

    lateinit var spinnerArrayAdapterCountry: ArrayAdapter<String>
    lateinit var spinnerArrayAdapterCity: ArrayAdapter<String>

    private val TAG = "TabActivity"

    lateinit var adapter: AdapterTabPager
    var kind = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        setSupportActionBar(tabToolbar)

        setSupportActionBar(tabToolbar)
        supportActionBar?.setTitle(getString(R.string.app_name))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        presenter.setView(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            var locationFine = ActivityCompat.checkSelfPermission(this@TabActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            var locationCoarse = ActivityCompat.checkSelfPermission(this@TabActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

            if (locationFine || locationCoarse) {
                Dexter.withActivity(this@TabActivity)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                                Snacky.builder()
                                        .setActivity(this@TabActivity)
                                        .setActionText(getString(R.string.tabSnackySettings))
                                        .setActionClickListener(object : View.OnClickListener {
                                            override fun onClick(v: View?) {
                                                openPermissionsSettings(this@TabActivity)
                                            }
                                        })
                                        .setText(getString(R.string.tabSnackyText))
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .build()
                                        .show()
                            }

                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                locationFine = ActivityCompat.checkSelfPermission(this@TabActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                locationCoarse = ActivityCompat.checkSelfPermission(this@TabActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                if (locationFine || locationCoarse) {
                                    Toast.makeText(this@TabActivity, getString(R.string.tabPermissions), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }).check()
            }

        }

        val intent = intent.extras
        kind = intent.getString("kind","")

        if(kind.contains("buddy-"))
            supportActionBar?.setTitle(getString(R.string.meetingsToolbarTitleUser))
        else if(kind.contains("group-"))
            supportActionBar?.setTitle(getString(R.string.meetingsToolbarTitleGroup))
        else if(kind.contains("place-"))
            supportActionBar?.setTitle(getString(R.string.meetingsToolbarTitlePlace))
        else
            supportActionBar?.setTitle(getString(R.string.meetingsToolbarTitle))

        setTabLayout()

        if(savedInstanceState!=null)
            adapter.getItem(savedInstanceState.getInt("tab",0))

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> supportActionBar?.setTitle(getString(R.string.meetingsToolbarTitle))
                    1 -> supportActionBar?.setTitle(getString(R.string.groupsToolbarTitle))
                    2 -> supportActionBar?.setTitle(getString(R.string.placesToolbarTitle))
                    3 -> supportActionBar?.setTitle(getString(R.string.profileToolbarTitle))
                }
            }
        })
    }

    fun openPermissionsSettings(activity: Activity){
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun setData() {
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.tabMeetings)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.tabGroups)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.tabPlaces)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.tabProfile)))
    }


    private fun setTabLayout() {
        adapter = AdapterTabPager(supportFragmentManager,
                tab_layout.tabCount,
                presenter, kind)

        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        if(!kind.equals(""))
            menu.getItem(4).setVisible(true)
        menu.getItem(2).setVisible(true)
        return true
    }

    override fun onCitySelectedChoosed(city: String) {
        presenter.getUserProfile()?.let{
            it.regionId = presenter.getRegionId(city)
            presenter.modifyUserInFirebaseDB(it.id, it)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_region -> {
                DialogFactory.callbackCity = this
                DialogFactory.buildChooseRegionDialog(this,presenter.getRegionList()).show()
            }
            R.id.toolbar_clear -> {
                val intent = Intent(this, TabActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return true
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun successLoadingRegions() {
        Timber.d(TAG + " " + getString(R.string.tabSuccessLoadRegion))
    }

    override fun successChangingRegion() {
        presenter.clearPaper()
        val intent = Intent(this,TabActivity::class.java)
        intent.putExtra("kind",kind)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}
