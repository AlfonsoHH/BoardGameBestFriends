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
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory

/**
 * A simple [Fragment] subclass.
 */
class GroupsFragment : Fragment(),
        GroupsContract.View,
        View.OnClickListener,
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
        fab.setOnClickListener(this)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    fun setupRecycler() {
        rvGroupFragment.layoutManager = LinearLayoutManager(context)
        rvGroupFragment.adapter = adapter

        adapter.onGroupClickedListener = { group ->
            itemDetail(group.id)
        }

        adapter.onLongClickListener = { group ->
            presenter.getUserProfile()?.let {user ->
                context?.let { ctx ->
                    if (group.creator.equals(user.id)) {
                        DialogFactory.buildConfirmDialog(ctx, getString(R.string.groupsDialogDelete), Runnable { presenter.removeGroup(group) }).show()
                    } else {
                        DialogFactory.buildConfirmDialog(ctx, getString(R.string.groupsDialog), Runnable { presenter.removeUserFromGroup(group) }).show()
                    }
                }
            }
        }
    }

    fun startAddGroup(){
        val intent = Intent(context, AddGroupActivity::class.java)
        intent.putExtra("id","")
        startActivityForResult(intent,1)
    }

    fun itemDetail(id: String) {
        val intent = Intent(context, GroupDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.setView(null)
        super.onDestroy()
    }

    override fun setData(groups: ArrayList<Group>) {
        adapter.groupList.clear()
        adapter.groupList.addAll(groups)
        adapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fab -> { startAddGroup() }
        }
    }

    override fun showError(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBar?.setVisibility(isLoading)
        rvGroupFragment?.setVisibility(!isLoading)
        if (!isLoading)
            swipeContainerGroups?.isRefreshing = false
    }

    override fun onRefresh() {
        adapter.groupList.clear()
        presenter.getGroupsData()
        swipeContainerGroups?.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            showSuccess(R.string.groupsSuccessAdding)
            presenter.updateGroupsFromCache()
        }
    }

}
