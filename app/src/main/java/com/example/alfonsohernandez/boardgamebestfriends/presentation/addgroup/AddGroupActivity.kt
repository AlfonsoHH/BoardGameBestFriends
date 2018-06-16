package com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.User
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterMembers
import kotlinx.android.synthetic.main.activity_add_group.*
import javax.inject.Inject
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.alfonsohernandez.boardgamebestfriends.presentation.base.BasePermissionActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.messaging.RemoteMessage
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.io.File


class AddGroupActivity : BasePermissionActivity(),
        AddGroupContract.View,
        View.OnClickListener,
        BasePermissionActivity.PermissionCallback,
        DialogFactory.DialogInputCallback{

    private val TAG = "AddGroupActivity"

    var adapter = AdapterMembers()

    private var url: String = "url"

    var id = ""
    var action = ""

    var photoModified = false

    @Inject
    lateinit var presenter: AddGroupPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        setSupportActionBar(AddGroupToolbar)
        supportActionBar?.setTitle(getString(R.string.addGroupToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        val intent = intent.extras
        intent?.let {
            id = it.getString("id", "")
            action = it.getString("action", "")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this)

        super.callbackPermissions = this

        if(action.equals("modify")) {
            addGroupRV.setVisibility(false)
            fab.setVisibility(false)
            presenter.getGroupData(id)
            supportActionBar?.setTitle(getString(R.string.addGroupToolbarTitleModify))
        }

        fab.setOnClickListener(this)
        addGroupIVphoto.setOnClickListener(this)
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(this,rm)
        nf.allNotifications()
    }

    fun addGroup(){
        if(!addGroupETtitle.text.toString().equals("") && !addGroupETdescription.text.toString().equals("") && (!url.equals("url") || photoModified)) {

            val memberList = arrayListOf<String>()
            for (member in adapter.memberList) {
                memberList.add(member.id)
            }

            addGroupIVphoto.setDrawingCacheEnabled(true)
            addGroupIVphoto.buildDrawingCache()
            val bitmap = (addGroupIVphoto.getDrawable() as BitmapDrawable).getBitmap()

            val group = Group(id,
                    url,
                    addGroupETtitle.text.toString(),
                    addGroupETdescription.text.toString(),
                    presenter.getUserProfile()?.id ?: return,
                    0)

            if (action.equals("modify"))
                presenter.saveImage(group,
                        bitmap,
                        photoModified,
                        null)
            else
                presenter.saveImage(group,
                        bitmap,
                        photoModified,
                        memberList)
        }else{
            showError(R.string.addGroupErrorEmpty)
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
                .into(addGroupIVphoto)
    }

    override fun setData(group: Group) {
        Glide.with(this)
                .load(group.photo)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(addGroupIVphoto)
        url = group.photo

        addGroupETtitle.setText(group.title)
        addGroupETdescription.setText(group.subtitle)
    }

    override fun finishAddGroup() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    override fun setFriend(user: User) {
        adapter.memberList.add(user)
        adapter.notifyDataSetChanged()
    }

    fun setupRecycler() {
        addGroupRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        addGroupRV.adapter = adapter
    }

    override fun getDialogInput(input: String) {
        presenter.getFriendData(input)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addGroupIVphoto -> {
                askForPermissionsPhoto(this@AddGroupActivity)
            }
            R.id.fab -> {
                DialogFactory.callbackInput = this
                DialogFactory.buildInputDialog(this@AddGroupActivity,getString(R.string.addGroupAddDialog)).show()
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
        progressBarAddGroup?.setVisibility(isLoading)
        addGroupFLall?.setVisibility(!isLoading)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        menu.getItem(0).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_add -> { addGroup() }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            callbackPermissions?.onImageReceived(data,true)
            photoModified = true
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            callbackPermissions?.onImageReceived(data,false)
            photoModified = true
        }
    }

}
