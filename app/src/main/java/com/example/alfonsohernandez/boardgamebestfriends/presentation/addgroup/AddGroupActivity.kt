package com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
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
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import android.view.WindowManager


class AddGroupActivity : AppCompatActivity(), AddGroupContract.View {

    private val TAG = "AddGroupActivity"

    var adapter = AdapterMembers()

    private var url: String = "url"

    var id = ""
    var action = ""

    @Inject
    lateinit var presenter: AddGroupPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        setSupportActionBar(AddGroupToolbar)
        supportActionBar!!.setTitle(getString(R.string.addGroupToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if(savedInstanceState != null) {
            id = savedInstanceState.getString("id", "")
            action = savedInstanceState.getString("action", "")
        }else{
            var intent = intent.extras
            id = intent.getString("id", "")
            action = intent.getString("action", "")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this)

        if(action.equals("modify")) {
            addGroupRV.setVisibility(false)
            fab.setVisibility(false)
            presenter.getGroupData(id)
        }

        fab.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val builder = AlertDialog.Builder(this@AddGroupActivity)
                builder.setTitle(getString(R.string.addGroupAddDialog))

                val input = EditText(this@AddGroupActivity)
                input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                builder.setView(input)

                builder.setPositiveButton(getString(R.string.addGroupAcept)) { dialog, which -> presenter.getFriendData(input.text.toString()) }
                builder.setNegativeButton(getString(R.string.addGroupCancel)) { dialog, which -> dialog.cancel() }

                val dialog = builder.create()
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show()
            }
        })

        addGroupIVphoto.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, 1)
            }
        })
    }

    fun addGroup(){
        if(!addGroupETtitle.text.toString().equals("") && !addGroupETdescription.text.toString().equals("") && !url.equals("url")) {
            var memberList = arrayListOf<String>()
            for (member in adapter.memberList) {
                memberList.add(member.id)
            }
            if (action.equals("modify"))
                presenter.modifyGroupData(Group(id,
                        url,
                        addGroupETtitle.text.toString(),
                        addGroupETdescription.text.toString(),
                        presenter.getProfileData()!!.id))
            else
                presenter.saveGroupData(Group(id, url, addGroupETtitle.text.toString(), addGroupETdescription.text.toString(), presenter.getProfileData()!!.id), memberList)
        }else{
            showErrorEmpty()
        }
    }

    override fun setData(group: Group) {
        val decodedString = Base64.decode(group.photo, Base64.DEFAULT)
        val imagenJug = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        Bitmap.createScaledBitmap(imagenJug, 90, 90, false)
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), imagenJug)
        roundedBitmapDrawable.isCircular = true

        addGroupIVphoto.setImageDrawable(roundedBitmapDrawable)

        url = group.photo

        addGroupETtitle.setText(group.title)
        addGroupETdescription.setText(group.subtitle)
    }

    override fun finishAddGroup() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            val pickedImage = data.data
            val roundedDrawable = RoundedBitmapDrawableFactory.create(applicationContext.resources, BitmapFactory.decodeFile(presenter.getRealPathFromURI(applicationContext, pickedImage)))
            roundedDrawable.isCircular = true
            addGroupIVphoto.setImageDrawable(roundedDrawable)
            addGroupIVphoto.adjustViewBounds = true

            url = presenter.getUrlFromPhoto(contentResolver.query(pickedImage!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null))
        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    override fun addFriend(user: User) {
        adapter.memberList.add(user)
        adapter.notifyDataSetChanged()
    }

    override fun setupRecycler() {
        addGroupRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        addGroupRV.adapter = adapter
    }

    override fun showErrorBuddy() {
        Toast.makeText(this, getString(R.string.addGroupErrorBuddy), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAdding() {
        Toast.makeText(this, getString(R.string.addGroupErrorAddingGroup), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMembers() {
        Toast.makeText(this, getString(R.string.addGroupErrorMembers), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorAlready() {
        Toast.makeText(this, getString(R.string.addGroupErrorAlready), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorModify() {
        Toast.makeText(this, getString(R.string.addGroupErrorModify), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmpty() {
        Toast.makeText(this, getString(R.string.addGroupErrorEmpty), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarAddGroup.setVisibility(boolean)
        addGroupFLall.setVisibility(!boolean)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        menu.getItem(0).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_add -> {
                addGroup()
            }
        }
        return true
    }

}
