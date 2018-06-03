package com.example.alfonsohernandez.boardgamebestfriends.presentation.tab

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TabLayout
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


class TabActivity : AppCompatActivity(), TabContract.View{

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
        supportActionBar!!.setTitle(getString(R.string.app_name))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        injectDependencies()
        presenter.setView(this)

        if(savedInstanceState!=null)
            kind = savedInstanceState.getString("kind","")

        setTabLayout()

        if(savedInstanceState!=null)
            adapter.getItem(savedInstanceState!!.getInt("tab",0))
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        menu.getItem(2).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_region -> {
                chooseRegion()
            }
        }
        return true
    }

    fun chooseRegion(){
        var builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.dialog_region,null)
        builder.setView(view)

        var spinnerCountry: Spinner = view.findViewById(R.id.dialogRegionScountry)
        var spinnerCity: Spinner = view.findViewById(R.id.dialogRegionScity)

        spinnerArrayAdapterCountry = ArrayAdapter(this, android.R.layout.simple_spinner_item, presenter.getCountryList())
        spinnerArrayAdapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.setAdapter(spinnerArrayAdapterCountry)

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                spinnerArrayAdapterCity.clear()
                spinnerArrayAdapterCity.addAll(presenter.getCityList(spinnerCountry.selectedItem.toString()))
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        spinnerArrayAdapterCity = ArrayAdapter(this, android.R.layout.simple_spinner_item, presenter.getCityList(spinnerCountry.selectedItem.toString()))
        spinnerArrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.setAdapter(spinnerArrayAdapterCity)

        builder.setTitle(getString(R.string.tabDialogTitle))
        builder.setPositiveButton(getString(R.string.tabDialogAccept),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                var user = presenter.getUserProfile()
                presenter.modifyUserInFirebaseDB(user!!.id, User(user.id,user.email,user.userName,user.photo,user.service,presenter.getRegionId(spinnerCity.selectedItem.toString())))
            }
        })
        builder.setNegativeButton(getString(R.string.tabDialogCancel),object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
            }
        })
        var dialog: Dialog = builder.create()
        dialog.show()
    }

    override fun showErrorLoadingRegions() {
        Toast.makeText(this, getString(R.string.tabErrorRegion), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorSavingUser() {
        Toast.makeText(this, getString(R.string.tabErrorUser), Toast.LENGTH_SHORT).show()
    }

    override fun successLoadingRegions() {
        Log.d(TAG,getString(R.string.tabSuccessLoadRegion))
    }

    override fun successChangingRegion() {
        Toast.makeText(this, getString(R.string.tabSuccessRegion), Toast.LENGTH_SHORT).show()
    }
}
