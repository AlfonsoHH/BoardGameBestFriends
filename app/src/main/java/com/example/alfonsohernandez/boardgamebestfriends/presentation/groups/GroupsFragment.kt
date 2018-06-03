package com.example.alfonsohernandez.boardgamebestfriends.presentation.groups


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterGroups
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailActivity
import kotlinx.android.synthetic.main.fragment_groups.*
import javax.inject.Inject
import android.app.Activity
import android.util.Log


/**
 * A simple [Fragment] subclass.
 */
class GroupsFragment : Fragment(),
        GroupsContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: GroupsPresenter

    var adapter = AdapterGroups()

    private val TAG = "GroupsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()
        presenter.setView(this)
        setupRecycler()

        swipeContainerGroups.setOnRefreshListener(this)

        fab.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                startAddGroup()
            }
        })
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun setupRecycler() {
        rvGroupFragment.layoutManager = LinearLayoutManager(context)
        rvGroupFragment.adapter = adapter

        adapter.onGroupClickedListener = { group ->
            itemDetail(group.id)
        }

        adapter.onLongClickListener = { group ->
            if(group.creator.equals(presenter.getUserProfile()!!.id)){
                val alertDilog = AlertDialog.Builder(context!!.applicationContext).create()
                alertDilog.setTitle(getString(R.string.groupsAlert))
                alertDilog.setMessage(getString(R.string.groupsDialogDelete))

                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.groupsYes), { dialogInterface, i ->
                    presenter.removeUserFromGroup(group)
                    adapter.groupList.remove(group)
                    adapter.notifyDataSetChanged()
                })

                alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.groupsNo), { dialogInterface, i ->
                    alertDilog.dismiss()
                })
                alertDilog.show()
            }else {
                val alertDilog = AlertDialog.Builder(context!!.applicationContext).create()
                alertDilog.setTitle(getString(R.string.groupsAlert))
                alertDilog.setMessage(getString(R.string.groupsDialog))

                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.groupsYes), { dialogInterface, i ->
                    presenter.removeUserFromGroup(group)
                    adapter.groupList.remove(group)
                    adapter.notifyDataSetChanged()
                })

                alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.groupsNo), { dialogInterface, i ->
                    alertDilog.dismiss()
                })
                alertDilog.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clearData()
    }

    fun startAddGroup(){
        val intent = Intent(context, AddGroupActivity::class.java)
        intent.putExtra("id","")
        startActivityForResult(intent,1)
    }

    override fun itemDetail(id: String) {
        val intent = Intent(context, GroupDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    override fun clearData(){
        adapter.groupList.clear()
    }

    override fun setData(group: Group) {
        adapter.groupList.add(group!!)
        adapter.notifyDataSetChanged()
    }

    override fun removeGroup(group: Group) {
        adapter.groupList.remove(group)
        adapter.notifyDataSetChanged()
    }

    override fun showErrorLoading() {
        Toast.makeText(context, getString(R.string.groupsErrorLoading), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemovingUser() {
        Toast.makeText(context, getString(R.string.groupsErrorRemovingUser), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemovingGroup() {
        Toast.makeText(context, getString(R.string.groupsErrorDeRemoveGroup), Toast.LENGTH_SHORT).show()
    }

    override fun successRemovingGroup() {
        Toast.makeText(context, getString(R.string.groupsSuccessRemovingGroup), Toast.LENGTH_SHORT).show()
    }

    override fun successRemovingUser() {
        Toast.makeText(context, getString(R.string.groupsSuccessRemovingUser), Toast.LENGTH_SHORT).show()
    }

    override fun successAdding() {
        Toast.makeText(context, getString(R.string.groupsSuccessAdding), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(isLoading: Boolean) {
        progressBar.setVisibility(isLoading)
        rvGroupFragment.setVisibility(!isLoading)
        if (!isLoading)
            swipeContainerGroups.isRefreshing = false
    }

    override fun onRefresh() {
        presenter.getGroupsData()
        swipeContainerGroups.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
            successAdding()
    }

}
