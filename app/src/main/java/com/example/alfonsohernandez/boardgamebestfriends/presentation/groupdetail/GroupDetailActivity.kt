package com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterMembers
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.games.GamesActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.Snacktory
import com.google.firebase.messaging.RemoteMessage
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_group_detail.*
import javax.inject.Inject

class GroupDetailActivity : AppCompatActivity(), GroupDetailContract.View, View.OnClickListener {

    private val TAG = "GroupDetailActivity"
    var groupId: String = ""

    var adapter = AdapterMembers()

    var iAmTheCreator = false

    @Inject
    lateinit var presenter: GroupDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)

        setSupportActionBar(groupDetailToolbar)
        supportActionBar?.setTitle(getString(R.string.groupDetailToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        val extras = intent.extras
        extras?.let {
            groupId = it.getString("id")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this, groupId)

        groupDetailIVchat.setOnClickListener(this)
        groupDetailIVgames.setOnClickListener(this)
        groupDetailIVmeetings.setOnClickListener(this)
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(this,rm)
        nf.chat()
        nf.goToGroups()
        nf.goToGroupDetail()
        nf.goToMeetings()
        if(groupId.equals(nf.topic)) {
            if (rm.notification!!.title!!.contains("Group user")) {
                presenter.getMembersData(groupId)
                Snacktory.snacktoryNoAction(this, nf.text)
            }
        }else{
            nf.goToMeetingDetail()
        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    fun setupRecycler() {
        groupDetailRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        groupDetailRV.adapter = adapter

        adapter.onMemberClickedListener = { member ->
            itemDetail(member.id)
        }
    }

    fun itemDetail(id: String){
        val intent = Intent(this, GamesActivity::class.java)
        intent.putExtra("kind", "buddy-" + id)
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    fun modifyGroup() {
        val intent = Intent(this, AddGroupActivity::class.java)
        intent.putExtra("id", groupId)
        intent.putExtra("action", "modify")
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            presenter.getGroupData(groupId)
            showSuccess(R.string.groupDetailSuccessModify)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setGroupData(group: Group) {

        if (!group.photo.equals("url")) {
            Glide.with(this)
                    .load(group.photo)
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(groupDetailIVphotoGroup)
        }

        groupDetailTVgruopTitle.text = group.title
        groupDetailTVdescription.text = group.subtitle

        if (group.creator.equals(presenter.getUserProfile()?.id)) {
            iAmTheCreator = true
            fab.setVisibility(true)
            fab.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val builder = AlertDialog.Builder(this@GroupDetailActivity)
                    builder.setTitle(getString(R.string.groupDetailDialog))

                    val input = EditText(applicationContext)
                    input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    builder.setView(input)

                    builder.setPositiveButton(getString(R.string.groupDetailDialogAccept)) { dialog, which -> presenter.getFriendFromMailData(input.text.toString()) }
                    builder.setNegativeButton(getString(R.string.groupDetailDialogCancel)) { dialog, which -> dialog.cancel() }

                    val dialog = builder.create()
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                    dialog.show()
                }
            })
        }
    }

    override fun setFriendData(user: User) {
        adapter.memberList.add(user)
        adapter.notifyDataSetChanged()
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarGroupDetail?.setVisibility(isLoading)
        groupDetailFLall?.setVisibility(!isLoading)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.groupDetailIVmeetings -> {
                val intent = Intent(this, TabActivity::class.java)
                intent.putExtra("tab", 0)
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
