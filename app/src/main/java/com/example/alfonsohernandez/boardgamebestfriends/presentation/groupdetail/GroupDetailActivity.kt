package com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterMembers
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import kotlinx.android.synthetic.main.activity_group_detail.*
import java.lang.Thread.sleep
import javax.inject.Inject

class GroupDetailActivity : AppCompatActivity(), GroupDetailContract.View, View.OnClickListener {

    private val TAG = "GroupDetailActivity"
    var groupId: String = ""

    var adapter = AdapterMembers()
    lateinit var menu: Menu

    @Inject
    lateinit var presenter: GroupDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)

        setSupportActionBar(groupDetailToolbar)
        supportActionBar!!.setTitle(getString(R.string.groupDetailToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if(savedInstanceState != null) {
            groupId = savedInstanceState.getString("id", "")
            Log.d(TAG,groupId)
        }else{
            val extras = intent.extras
            groupId = extras.getString("id")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this,groupId)

        groupDetailIVchat.setOnClickListener(this)
        groupDetailIVgames.setOnClickListener(this)
        groupDetailIVmeetings.setOnClickListener(this)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null,"")
        super.onDestroy()
    }

    fun modifyGroup(){
        val intent = Intent(this, AddGroupActivity::class.java)
        intent.putExtra("id",groupId)
        intent.putExtra("action","modify")
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            presenter.getGroupData(groupId)
            successModify()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setGroupData(group: Group) {

        if(!group.photo.equals("url")) {
            val decodedString = Base64.decode(group.photo, Base64.DEFAULT)
            val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Bitmap.createScaledBitmap(imagenJug, 90, 90, false)
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
            roundedBitmapDrawable.isCircular = true

            groupDetailIVphotoGroup.setImageDrawable(roundedBitmapDrawable)
        }

        groupDetailTVgruopTitle.text = group.title
        groupDetailTVdescription.text = group.subtitle

        if(group.creator.equals(presenter.getUserProfile()!!.id)) {
            fab.setVisibility(true)
            fab.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val builder = AlertDialog.Builder(applicationContext)
                    builder.setTitle(getString(R.string.groupDetailDialog))

                    val input = EditText(applicationContext)
                    input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    builder.setView(input)

                    builder.setPositiveButton(getString(R.string.groupDetailDialogAccept)) { dialog, which -> presenter.getFriendData(input.text.toString()) }
                    builder.setNegativeButton(getString(R.string.groupDetailDialogCancel)) { dialog, which -> dialog.cancel() }

                    builder.show()
                }
            })
        }else{
            var menuItem = menu.findItem(R.id.toolbar_modify)
            menuItem.setVisible(false)
        }
    }

    override fun setRegionData(region: Region) {
        groupDetailTVresidence.text = region.city + ", " + region.country
    }

    override fun setFriendData(user: User) {
        Log.d(TAG,user.photo)
        adapter.memberList.add(user)
        adapter.notifyDataSetChanged()
    }

    override fun clearFriendsData() {
        adapter.memberList.clear()
    }

    override fun showErrorGroup() {
        Toast.makeText(this, getString(R.string.groupDetailErrorGroup), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMembers() {
        Toast.makeText(this, getString(R.string.groupDetailErrorMembers), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRegion() {
        Toast.makeText(this, getString(R.string.groupDetailErrorRegion), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorNewMember() {
        Toast.makeText(this, getString(R.string.groupDetailErrorNewMember), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAlreadyExist() {
        Toast.makeText(this, getString(R.string.groupDetailErrorAlready), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorDoesNotExist() {
        Toast.makeText(this, getString(R.string.groupDetailErrorExist), Toast.LENGTH_SHORT).show()
    }

    override fun successNewMember() {
        Toast.makeText(this, getString(R.string.groupDetailSuccess), Toast.LENGTH_SHORT).show()
    }

    override fun successModify() {
        Toast.makeText(this, getString(R.string.groupDetailSuccessModify), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarGroupDetail.setVisibility(boolean)
        groupDetailFLall.setVisibility(!boolean)
    }

    override fun setupRecycler() {
        groupDetailRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        groupDetailRV.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.groupDetailIVmeetings -> {
                val intent = Intent(this, TabActivity::class.java)
                intent.putExtra("tab",0)
                intent.putExtra("kind", "group-" + groupId)
                startActivity(intent)
            }
            R.id.groupDetailIVgames -> {
                val intent = Intent(this, GamesActivity::class.java)
                intent.putExtra("kind", "group-" + groupId)
                startActivity(intent)
            }
            R.id.groupDetailIVchat -> {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("id", groupId)
                startActivity(intent)
            }
        }
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
